package com.lenaebner.sharetime

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.lenaebner.sharetime.databinding.HomeFragmentBinding
import com.lenaebner.sharetime.databinding.ProfileFragmentBinding

class ProfileFragment : Fragment(R.layout.profile_fragment) {

    private val args: ProfileFragmentArgs by navArgs()
    private val adapter = ProfilePostsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = ProfileFragmentBinding.bind(view)

        binding.postList.adapter = adapter

        val db = Firebase.firestore
        val users = db.collection("users")
        users.document(args.userId).get().addOnSuccessListener { value ->
            val user = value.toObject<Person>()

            binding.run {
                name.text = args.username
                description.text = "This could be a useful description text about me. But it isn't. "

                profilePicture.load(user?.profilePicture)
                editButton.setOnClickListener{

                    //Navigate with directions
                    findNavController().navigate(ProfileFragmentDirections.profileToEditProfile(args.userId))
                }

            }

            val posts = db.collection("posts").whereEqualTo("author.full_name",args.username)
                .addSnapshotListener { value, error ->

                    val userposts = value?.toObjects<Post>().orEmpty()
                    adapter.submitList(userposts)
                    binding.postNr.text = userposts.size.toString()
                }
        }
    }
}