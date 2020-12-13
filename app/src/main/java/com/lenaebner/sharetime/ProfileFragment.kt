package com.lenaebner.sharetime

import android.R
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.firebase.ui.auth.AuthUI

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.lenaebner.sharetime.databinding.ProfileFragmentBinding

class ProfileFragment : Fragment(com.lenaebner.sharetime.R.layout.profile_fragment) {

    private val args: ProfileFragmentArgs by navArgs()
    private val adapter = ProfilePostsAdapter()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = ProfileFragmentBinding.bind(view)
        binding.postList.adapter = adapter

        setHasOptionsMenu(true)
        val db = Firebase.firestore
        val users = db.collection("users")
        users.document(args.userId).get().addOnSuccessListener { value ->
            val user = value.toObject<Person>()

            binding.run {
                name.text = args.username
                description.text = user?.description.orEmpty()
                followersNr.text = user?.followers?.size.toString()
                followingNr.text = user?.following?.size.toString()

                profilePicture.load(user?.profilePicture) {
                    transformations(CircleCropTransformation())
                }
            }

            if(args.userId == Firebase.auth.currentUser?.uid.toString()) {
                binding.run {
                    editButton.visibility = View.VISIBLE
                    followButton.visibility = View.INVISIBLE
                    editButton.setOnClickListener{

                        //Navigate with directions
                        findNavController().navigate(
                            ProfileFragmentDirections.profileToEditProfile(
                                args.userId
                            )
                        )
                    }
                }
            }
            else {
                binding.run {
                    editButton.visibility = View.INVISIBLE
                    followButton.visibility = View.VISIBLE

                    //check if the auth.currnet user is following the user of args
                    val followers = user?.followers.orEmpty()
                    if(followers.contains(users.document(Firebase.auth.currentUser?.uid.toString()))) {
                        followButton.text = "unfollow"
                        followButton.setOnClickListener{
                            users.document(Firebase.auth.currentUser?.uid.orEmpty())
                                .update( "following", FieldValue.arrayRemove(value.reference))

                            users.document(args.userId).update(
                                "followers", FieldValue.arrayRemove(users.document(Firebase.auth.currentUser?.uid.orEmpty()))
                            )
                            followButton.text = "follow"
                        }
                    }
                    else {
                        followButton.text = "follow"
                        followButton.setOnClickListener{
                            users.document(Firebase.auth.currentUser?.uid.orEmpty())
                                .update( "following", FieldValue.arrayUnion(value.reference))

                            users.document(args.userId).update(
                                "followers", FieldValue.arrayUnion(users.document(Firebase.auth.currentUser?.uid.orEmpty()))
                            )

                            followButton.text = "unfollow"
                        }
                    }


                }
            }

            val posts = db.collection("posts").whereEqualTo("author.uid", args.userId)
                .addSnapshotListener { value, error ->

                    val userposts = value?.toObjects<Post>().orEmpty()
                    adapter.submitList(userposts)
                    binding.postNr.text = userposts.size.toString()
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(com.lenaebner.sharetime.R.menu.profile, menu)
    }

    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == com.lenaebner.sharetime.R.id.logout) {
            //Logout using firebase UI
            AuthUI.getInstance().signOut(requireContext()).addOnCompleteListener {
                findNavController().navigate(ProfileFragmentDirections.profileToLogin())
            }
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}