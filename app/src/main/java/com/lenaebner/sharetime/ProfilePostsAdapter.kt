package com.lenaebner.sharetime

import android.media.Image
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.lenaebner.sharetime.databinding.ProfilePostBinding

class ProfilePostsAdapter : ListAdapter<Post, ProfilePostsAdapter.PostViewHolder>(DIFF_UTIL) {

    inner class PostViewHolder(private val binding: ProfilePostBinding ) :
        RecyclerView.ViewHolder(binding.root) {

            fun bindPost(post: Post) {
                binding.postImg.load(post.imageUrl)
                binding.postImg.setOnClickListener {
                    it.findNavController().navigate(ProfileFragmentDirections.profileToComment(post.documentId))
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ProfilePostBinding.inflate(layoutInflater, parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = currentList[position]

        holder.bindPost(post)
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