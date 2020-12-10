package com.lenaebner.sharetime

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import coil.load
import com.lenaebner.sharetime.databinding.SingleCommentBinding

class CommentAdapter  : ListAdapter<Comment, CommentAdapter.CommentViewHolder>(DIFF_UTIL){

    inner class CommentViewHolder(private val binding: SingleCommentBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bindComment(comment: Comment) {
                    binding.comment.text = comment.text
                    binding.profileImg.load(comment.author.profilePicture)
                    binding.userName.text = comment.author.fullName
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