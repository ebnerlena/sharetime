package com.lenaebner.sharetime

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.lenaebner.sharetime.databinding.SinglePostBinding

class PostAdapter : ListAdapter<Post, PostAdapter.PostViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SinglePostBinding.inflate(layoutInflater, parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    class PostViewHolder(private val binding: SinglePostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var hasLiked: Boolean = false

        fun bind(post: Post) {

            binding.run {

                postImg.load(post.imageUrl)
                commentNr.text = "3" // post.comments.toString()
                likeNr.text = "3" //post.likes.toString()
                userName.text = post.author.fullName
                profileImg.load(post.author.profilePicture) {
                    CircleCropTransformation()
                }

                likeImg.setOnClickListener {
                    hasLiked = !hasLiked

                    if (hasLiked) {
                        likeImg.load(R.drawable.like_filled)
                        likeNr.text = Integer.parseInt(likeNr.text.toString()).plus(1).toString()
                    } else {
                        likeImg.load(R.drawable.like_unfilled)
                        likeNr.text = Integer.parseInt(likeNr.text.toString()).minus(1).toString()
                    }
                }

                profileImg.setOnClickListener {
                    it.findNavController().navigate(HomeFragmentDirections.homeToProfile(post.author.fullName,post.author.uid))
                }

                userName.setOnClickListener {
                    it.findNavController().navigate(HomeFragmentDirections.homeToProfile(post.author.fullName, post.author.uid))
                }

                commentImg.setOnClickListener {
                    it.findNavController().navigate(HomeFragmentDirections.homeToComments(post.author.uid, post.text))
                }
            }
        }
    }

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }
        }
    }


}