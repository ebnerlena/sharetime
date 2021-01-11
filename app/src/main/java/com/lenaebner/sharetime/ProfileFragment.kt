package com.lenaebner.sharetime

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.lenaebner.sharetime.databinding.EditProfileFragmentBinding
import com.lenaebner.sharetime.databinding.ProfileFragmentBinding

class ProfileFragment : Fragment(R.layout.profile_fragment) {

    private lateinit var binding: ProfileFragmentBinding
    private val args: ProfileFragmentArgs by navArgs()
    private val adapter = ProfilePostsAdapter()
    private val users = Firebase.firestore.collection("people")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = ProfileFragmentBinding.bind(view)

        val currentUser = Firebase.auth.currentUser

        if(currentUser == null) {
            findNavController().navigate(ProfileFragmentDirections.profileToLogin())
        }

        binding.postList.adapter = adapter
        binding.name.text = args.username

        setHasOptionsMenu(true)

        users.document(args.userId).addSnapshotListener { value,_ ->
            val user = value?.toObject<Person>()

            if (user != null) {
                setBinding(user, binding)

                if(user.location.isNotEmpty()) {
                    binding.location.text = user.location
                } else {
                    binding.locationIcon.isVisible = false
                }
            }

            if(args.userId == Firebase.auth.currentUser?.uid.toString()) {
                setOwnProfile(binding)
            }
            else {
                if (user != null) {
                    setOtherProfile(user, binding, value)
                }
            }
            manageUserPosts(binding)
        }
    }

    private fun setBinding(user: Person, binding: ProfileFragmentBinding){

        binding.run {
            name.text = args.username
            description.text = user?.description?.toString()
            followersNr.text = user?.followers?.size.toString()
            followingNr.text = user?.following?.size.toString()

            if(user?.profilePicture.isNullOrEmpty()){
                profilePicture.load(R.drawable.person_grey)
            }  else {
                profilePicture.load(user?.profilePicture) {
                    transformations(CircleCropTransformation())
                }
            }
        }
    }

    private fun refreshBinding(binding: ProfileFragmentBinding) {

        users.document(args.userId).get().addOnSuccessListener { value ->
            val user = value.toObject<Person>()

            if (user != null) {
                setBinding(user, binding)
                setOtherProfile(user, binding, value)
            }
        }
    }

    private fun manageUserPosts(binding: ProfileFragmentBinding) {

        val posts = Firebase.firestore.collection("posts")
                .whereEqualTo("author.uid", args.userId)
                .addSnapshotListener { value, error ->

                    val userPosts = value?.toObjects<Post>().orEmpty()
                    adapter.submitList(userPosts.sortedByDescending {post -> post.timestamp })
                    binding.postNr.text = userPosts.size.toString()

                    if(userPosts.isNullOrEmpty()){
                        binding.noPosts.visibility = View.VISIBLE
                    }
                }

        binding.addPhoto.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.profileToNewPost())
        }
    }

    private fun setOwnProfile(binding: ProfileFragmentBinding){

        binding.run {
            editButton.visibility = View.VISIBLE
            followButton.visibility = View.INVISIBLE
            editButton.setOnClickListener {

                findNavController().navigate(
                        ProfileFragmentDirections.profileToEditProfile()                        )
            }
        }
    }

    private fun setOtherProfile(user: Person, binding: ProfileFragmentBinding, value: DocumentSnapshot) {
        binding.run {
            editButton.isVisible = false
            followButton.isVisible = true

            val followers = user?.followers.orEmpty()

            if(authUserIsInList(followers)) {
                followSetup(binding, value)
            }
            else {
                unfollowSetup(binding,value)
            }
        }
    }

    private fun authUserIsInList(list: List<DocumentReference>) : Boolean {
        return list.contains(users.document(Firebase.auth.currentUser?.uid.toString()))
    }

    private fun followSetup(binding: ProfileFragmentBinding, value: DocumentSnapshot) {

        binding.run {

            followButton.text = "unfollow"
            followButton.setOnClickListener{
                users.document(Firebase.auth.currentUser?.uid.orEmpty())
                        .update( "following", FieldValue.arrayRemove(value.reference))

                users.document(args.userId).update(
                        "followers", FieldValue.arrayRemove(users.document(Firebase.auth.currentUser?.uid.orEmpty()))
                )
                followButton.text = "follow"
                refreshBinding(binding)
            }
        }
    }
    private fun unfollowSetup(binding: ProfileFragmentBinding, value: DocumentSnapshot) {
        binding.run {
            followButton.text = "follow"
            followButton.setOnClickListener{
                users.document(Firebase.auth.currentUser?.uid.orEmpty())
                        .update( "following", FieldValue.arrayUnion(value.reference))

                users.document(args.userId).update(
                        "followers", FieldValue.arrayUnion(users.document(Firebase.auth.currentUser?.uid.orEmpty()))
                )

                followButton.text = "unfollow"
                refreshBinding(binding)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            //Logout using firebase UI
            AuthUI.getInstance().signOut(requireContext()).addOnCompleteListener {
                findNavController().navigate(ProfileFragmentDirections.profileToLogin())
            }
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}