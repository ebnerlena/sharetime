package com.lenaebner.sharetime

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.GrayscaleTransformation
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.lenaebner.sharetime.databinding.CommentsFragmentBinding

class CommentsFragment : Fragment(R.layout.comments_fragment){
    private val args: CommentsFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = CommentsFragmentBinding.bind(view)

        val adapter = CommentAdapter()

        val db = Firebase.firestore
        db.collection("users").document(args.userId).get().addOnSuccessListener {
            val user = it?.toObject<Author>();

            binding.run {
                commentsList.adapter = adapter
                description.text = args.posttext

                userName.text = user?.fullName
                val name = user?.fullName.orEmpty()
                userName.setOnClickListener {
                    findNavController().navigate(CommentsFragmentDirections.commentToProfile(name, args.userId))
                }
                profileImg.load(user?.profilePicture) {
                    transformations(CircleCropTransformation())
                }
                profileImg.setOnClickListener {
                    findNavController().navigate(CommentsFragmentDirections.commentToProfile(name, args.userId))
                }

                    submit.setOnClickListener {
                    //todo: post new comment
                }

            }


        }

    }
}