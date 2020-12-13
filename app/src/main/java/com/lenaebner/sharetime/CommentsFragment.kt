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

        db.collection("users").document(args.userId).get().addOnSuccessListener {
            val user = it?.toObject<Author>();

            var post = Post()
            db.collection("posts").whereEqualTo("text", args.posttext).addSnapshotListener {value, _ ->
                val posts = value?.toObjects<Post>().orEmpty()
                post = posts[0]

                var commentsList = mutableListOf<Comment>()
                for (comment in post.comments) {
                    db.collection("comments").document(comment.id).get().addOnSuccessListener { value ->
                        var c = value.toObject<Comment>()!!
                        commentsList.add(c)
                    }
                }
                adapter.submitList(commentsList)
                binding.commentsList.smoothScrollToPosition(commentsList.size)
            }

            binding.run {
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
                    val commentText = binding.commentInput.text.toString()
                    if(commentText != "") {
                        addComment(post, commentText)
                        binding.commentInput.text?.clear()
                        binding.commentInput.clearFocus()
                    }
                }
            }
        }
    }

    private fun addComment(post: Post, commentText:String) {

        var newComment = Comment()
        val currentUser = Firebase.auth.currentUser

        db.collection("users").document(currentUser?.uid.orEmpty()).get().addOnSuccessListener { value ->
            val author = value.toObject<Author>()!!
            author?.uid = currentUser?.uid.orEmpty()

            newComment = Comment(author, commentText)

            db.collection("comments").add(newComment).addOnSuccessListener { c ->

                    db.collection("posts").whereEqualTo("timestamp", post.timestamp).get().addOnSuccessListener {
                        db.collection("posts")
                                .document(it.documents[0].id)
                                .update("comments", FieldValue.arrayUnion(c))
                    }
                }
            }
        }
}