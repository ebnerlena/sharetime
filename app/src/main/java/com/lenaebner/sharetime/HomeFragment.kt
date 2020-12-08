package com.lenaebner.sharetime

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.lenaebner.sharetime.databinding.HomeFragmentBinding

class HomeFragment : Fragment(R.layout.home_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding =  HomeFragmentBinding.bind(view)
        val adapter = PostAdapter()
        binding.postList.adapter = adapter

         val db = Firebase.firestore

        /*db.collection("posts").get().addOnSuccessListener {
            val posts = it?.toObjects<Post>().orEmpty()
            print(posts[0].text)
            adapter.submitList(posts)
        } */

       db.collection("posts").orderBy("timestamp").addSnapshotListener{ value, error ->
            val posts = value?.toObjects<Post>().orEmpty()
           adapter.submitList(posts)
        }
    }
}