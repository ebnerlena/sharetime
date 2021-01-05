package com.lenaebner.sharetime

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import coil.load
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lenaebner.sharetime.databinding.SingleCommentBinding
import java.text.DateFormat
import java.text.SimpleDateFormat

class CommentAdapter  : ListAdapter<Comment, CommentAdapter.CommentViewHolder>(DIFF_UTIL){

    inner class CommentViewHolder(private val binding: SingleCommentBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bindComment(comment: Comment) {
                    binding.comment.text = comment.text

                    if(comment.author.profilePicture.isNullOrEmpty()) {
                        binding.profileImg.load(R.drawable.person_grey)

                    } else {
                        binding.profileImg.load(comment.author?.profilePicture) {
                            fallback(R.drawable.person_grey)
                            transformations(CircleCropTransformation())
                        }
                    }

                    val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)
                    binding.timestamp.text = dateFormat.format(comment?.timestamp?.toDate())
                    binding.userName.text = comment.author.fullName

                    binding.profileImg.setOnClickListener {
                        it.findNavController().navigate(CommentsFragmentDirections.commentsToProfile(comment.author.fullName, comment.author.uid))
                    }
                }
            }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = getItem(position)
        holder.bindComment(comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SingleCommentBinding.inflate(layoutInflater, parent, false)
        return CommentViewHolder((binding))
    }

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem == newItem
            }
        }
    }

}