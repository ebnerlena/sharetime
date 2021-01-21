package com.lenaebner.sharetime

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.lenaebner.sharetime.databinding.HomeFragmentBinding
import com.lenaebner.sharetime.firestore.allPosts
import com.lenaebner.sharetime.firestore.currentUser
import com.lenaebner.sharetime.firestore.user

class HomeFragment : Fragment(R.layout.home_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding =  HomeFragmentBinding.bind(view)
        val adapter = PostAdapter()
        binding.postList.adapter = adapter

        binding.addPhoto.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToNewPost())
        }

        val db = Firebase.firestore
        val postsRef = db.allPosts().orderBy("timestamp", Query.Direction.DESCENDING)
        postsRef.addSnapshotListener{ value, error ->
            var posts = value?.toObjects<Post>().orEmpty()

           val userRef = db.user(db.currentUser().id).get()
           userRef.addOnSuccessListener {
               val user = it?.toObject<Person>()
               posts.sortedBy{ post -> user?.following?.contains(post.author.uid) == true }
               adapter.submitList(posts)

           }
            userRef.addOnFailureListener {
                Snackbar.make(binding.root ,"Are you logged in ?...", Snackbar.LENGTH_SHORT ).show()
            }
        }
    }
}
