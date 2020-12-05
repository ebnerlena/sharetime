package com.lenaebner.sharetime

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.lenaebner.sharetime.databinding.HomeFragmentBinding
import com.lenaebner.sharetime.databinding.ProfileFragmentBinding

class ProfileFragment : Fragment(R.layout.profile_fragment) {

    private val args: ProfileFragmentArgs by navArgs()
    private var user = Person()
    private val adapter = ProfilePostsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = ProfileFragmentBinding.bind(view)

        binding.postList.layoutManager = GridLayoutManager(activity, 2)
        binding.postList.adapter = adapter


        val db = Firebase.firestore
        val users = db.collection("users")
        users.whereEqualTo("full_name", args.username).addSnapshotListener { value, error ->
            val users = value?.toObjects<Person>().orEmpty()
            user = users?.get(0)

            binding.run {
                name.text = user.fullName
                description.text = "This could be a useful description text about me. But it isn't. "

                profilePicture.load(user.profilePicture)

                editButton.setOnClickListener{

                    //Navigate with directions
                    findNavController().navigate(ProfileFragmentDirections.profileToEditProfile(user.fullName))
                }

            }

            val posts = db.collection("posts").whereEqualTo("author.full_name", user.fullName)
                .addSnapshotListener { value, error ->

                    val posts = value?.toObjects<Post>().orEmpty()
                    adapter.submitList(posts)
                }
        }
    }

    private val colors = listOf(Color.CYAN, Color.GRAY, Color.MAGENTA, Color.RED, Color.YELLOW)
    private fun getRandomColor(): Int = colors.random()
}