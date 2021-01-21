package com.lenaebner.sharetime

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.type.Date
import com.lenaebner.sharetime.databinding.CommentsFragmentBinding
import com.lenaebner.sharetime.firestore.allPosts
import com.lenaebner.sharetime.firestore.currentUser
import com.lenaebner.sharetime.firestore.postComments
import com.lenaebner.sharetime.firestore.users
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class CommentsFragment : Fragment(R.layout.comments_fragment){
    private val args: CommentsFragmentArgs by navArgs()
    private val db = Firebase.firestore

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = CommentsFragmentBinding.bind(view)
        val adapter = CommentAdapter()
        binding.commentsList.adapter = adapter

        val currentUser = db.currentUser()

        if(currentUser == null) {
            findNavController().navigate(CommentsFragmentDirections.commentsToLogin())
        }

        db.users().document(currentUser.id).addSnapshotListener { value, _ ->
            val user = value?.toObject<Author>()

            db.allPosts().document(args.postId).addSnapshotListener { p, _ ->
                val post = p?.toObject<Post>()

                binding.run {
                    description.text = post?.text

                    if(post?.author?.profilePicture.isNullOrEmpty()){
                        profileImg.load(R.drawable.person_grey)
                    }  else {
                        profileImg.load(post?.author?.profilePicture) {
                            transformations(CircleCropTransformation())
                        }
                    }
                    postImg.load(post?.imageUrl)


                    val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)
                    timestamp.text = dateFormat.format(post?.timestamp?.toDate())

                    val name = post?.author?.fullName.orEmpty()

                    profileImg.setOnClickListener {
                        findNavController().navigate(CommentsFragmentDirections.commentsToProfile(name, post?.author?.uid.toString()))
                    }

                    submit.setOnClickListener {
                        val commentText = binding.commentInput.text.toString()
                        if(commentText != "") {
                            if (user != null) {
                                addComment(user, commentText)
                            }
                            binding.commentInput.text?.clear()
                         }
                    }
                }
            }

            db.postComments(args.postId).addSnapshotListener { c, _ ->
                val comments = c?.toObjects<Comment>().orEmpty()
                adapter.submitList(comments)
                binding.commentsList.smoothScrollToPosition(comments.size)
            }
        }
    }

    private fun addComment(author: Author, commentText: String) {
        author.uid = Firebase.auth?.currentUser?.uid ?: return
        author.uid = Firebase.auth?.currentUser?.uid.toString()
        val newComment = Comment(author, commentText)
        db.postComments(args.postId).add(newComment)
    }
}