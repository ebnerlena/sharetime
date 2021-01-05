package com.lenaebner.sharetime

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.lenaebner.sharetime.databinding.HomeFragmentBinding

class HomeFragment : Fragment(R.layout.home_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding =  com.lenaebner.sharetime.databinding.HomeFragmentBinding.bind(view)
        val adapter = PostAdapter()
        binding.postList.adapter = adapter

        binding.addPhoto.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToNewPost())
        }

        val db = Firebase.firestore

       db.collection("posts").orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener{ value, error ->
            var posts = value?.toObjects<Post>().orEmpty()

           val userRef = db.collection("people").document(Firebase.auth.currentUser?.uid.toString())
           userRef.get().addOnSuccessListener {
               val user = it?.toObject<Person>()

               //not working cause following contains user references not strings
               /*posts.sortedBy{ post -> user?.following?.contains(post.author.uid) == true }
               adapter.submitList(posts)*/

               var sortedFollowingPosts = mutableListOf<Post>()
               val postsList = posts.toMutableList()

               //sort the list by timestamp and followings
               posts.forEachIndexed { index, post ->
                   user?.following?.forEach {
                       if(it.id == "${post.author.uid}") {
                           sortedFollowingPosts.add(post)
                           postsList.remove(post)
                       }
                   }
               }
               adapter.submitList(sortedFollowingPosts+postsList)

           }
        }
    }
}
