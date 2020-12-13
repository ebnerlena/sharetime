package com.lenaebner.sharetime

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.lenaebner.sharetime.databinding.NewPostFragmentBinding

class NewPostFragment : Fragment(R.layout.new_post_fragment) {
    private val db = Firebase.firestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = NewPostFragmentBinding.bind(view)

        binding.run {
            binding.postPicture.load("https://cdn.pixabay.com/photo/2015/12/01/20/28/green-1072828_960_720.jpg")
            post.setOnClickListener {

                db.collection("users").document(Firebase.auth.currentUser?.uid.orEmpty()).get().addOnSuccessListener { value ->
                    val author = value.toObject<Author>()
                    author?.uid = Firebase.auth.currentUser?.uid.orEmpty()
                    var user = value.toObject<Person>()
                    val des = description.text.toString()

                    val post = author?.let { it -> Post("https://cdn.pixabay.com/photo/2015/12/01/20/28/green-1072828_960_720.jpg", it, des) }

                    if (post != null) {
                        db.collection("posts").add(post).addOnSuccessListener {

                            db.collection("users").document(Firebase.auth.currentUser?.uid.orEmpty())
                                .update( "posts", FieldValue.arrayUnion(it)
                            )
                        }
                    }
                }

                findNavController().navigate(R.id.homeFragment)
            }
        }
    }

}