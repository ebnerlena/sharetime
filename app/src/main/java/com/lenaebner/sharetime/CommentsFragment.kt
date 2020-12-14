package com.lenaebner.sharetime

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.GrayscaleTransformation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.lenaebner.sharetime.databinding.CommentsFragmentBinding

class CommentsFragment : Fragment(R.layout.comments_fragment){
    private val args: CommentsFragmentArgs by navArgs()
    private val db = Firebase.firestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = CommentsFragmentBinding.bind(view)
        val adapter = CommentAdapter()
        binding.commentsList.adapter = adapter

        val currentUser = Firebase.auth.currentUser

        if(currentUser == null) {
            findNavController().navigate(CommentsFragmentDirections.commentsToLogin())
        }

        db.collection("users").document(currentUser?.uid.toString()).addSnapshotListener { value, _ ->
            val user = value?.toObject<Author>() !!

            db.collection("posts").document(args.postId).addSnapshotListener { p, _ ->
                val post = p?.toObject<Post>()

                binding.run {
                    description.text = post?.text
                    userName.text = post?.author?.fullName
                    profileImg.load(post?.author?.profilePicture) {
                        fallback(R.drawable.person)
                    }

                    val name = user.fullName.orEmpty()
                    userName.setOnClickListener {
                        findNavController().navigate(CommentsFragmentDirections.commentsToProfile(name, user.uid))
                    }
                    profileImg.load(user?.profilePicture) {
                        transformations(CircleCropTransformation())
                    }
                    profileImg.setOnClickListener {
                        findNavController().navigate(CommentsFragmentDirections.commentsToProfile(name, user.uid))
                    }

                    submit.setOnClickListener {
                        val commentText = binding.commentInput.text.toString()
                        if(commentText != "") {
                            addComment(user, commentText)
                            binding.commentInput.text?.clear()
                            binding.commentInput.clearFocus()
                        }
                    }
                }
            }

            db.collection("posts").document(args.postId).collection("comments").addSnapshotListener {c, _ ->
                val comments = c?.toObjects<Comment>().orEmpty()
                adapter.submitList(comments)
                binding.commentsList.smoothScrollToPosition(comments.size)
            }

        }
    }


    private fun addComment(author: Author, commentText:String) {

        val newComment = Comment(author, commentText)
        db.collection("posts").document(args.postId).collection("comments").add(newComment)

    }
}