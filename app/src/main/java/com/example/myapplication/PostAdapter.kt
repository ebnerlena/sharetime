package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.myapplication.databinding.SinglePostBinding

class PostAdapter : ListAdapter<Int, PostAdapter.PostViewHolder>(DIFF_UTIL) {

    val fakePosts = listOf(
        PostData(
            userId = 1,
            userName = "Celina",
            profileImage = "https://cdn.pixabay.com/photo/2013/07/13/13/40/aristocrat-161317_960_720.png",
            postImage = "https://cdn.pixabay.com/photo/2015/12/01/20/28/green-1072828_1280.jpg",
            likes = 50,
            comments = 5
        ),
        PostData(
            userId = 2,
            userName = "Josef",
            profileImage = "https://cdn.pixabay.com/photo/2013/07/13/13/41/tux-161391_1280.png",
            postImage = "https://cdn.pixabay.com/photo/2016/07/22/16/29/fog-1535201_1280.jpg",
            likes = 13,
            comments = 9
        ),
        PostData(
            userId = 3,
            userName = "Simon",
            profileImage = "https://cdn.pixabay.com/photo/2013/07/13/13/42/tux-161406_960_720.png",
            postImage = "https://cdn.pixabay.com/photo/2018/11/17/22/15/tree-3822149_1280.jpg",
            likes = 44,
            comments = 2
        ),

        PostData(
            userId = 4,
            userName = "Eva",
            profileImage = "https://cdn.pixabay.com/photo/2013/07/13/13/40/painter-161318_1280.png",
            postImage = "https://cdn.pixabay.com/photo/2016/09/19/07/01/lake-irene-1679708_1280.jpg",
            likes = 77,
            comments = 2
        ),

        PostData(
            userId = 5,
            userName = "Sophie",
            profileImage = "https://cdn.pixabay.com/photo/2013/07/13/13/45/tux-161475_1280.png",
            postImage = "https://cdn.pixabay.com/photo/2017/01/03/19/41/forest-1950402_1280.jpg",
            likes = 10,
            comments = 20
        ),


        )

    fun getListCount():Int{
        return fakePosts.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SinglePostBinding.inflate(layoutInflater, parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = fakePosts[position]
        holder.bind(post)
    }

    class PostViewHolder(private val binding: SinglePostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var hasLiked: Boolean = false

        fun bind(post: PostData) {

            binding.run {

                postImg.load(post.postImage)
                commentNr.text = post.comments.toString()
                likeNr.text = post.likes.toString()
                userName.text = post.userName
                profileImg.load(post.profileImage) {
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
                    it.findNavController().navigate(HomeFragmentDirections.homeToProfile(post.userId))
                }

                userName.setOnClickListener {
                    it.findNavController().navigate(HomeFragmentDirections.homeToProfile(post.userId))
                }
            }
        }
    }

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Int>() {
            override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
                return oldItem == newItem
            }
        }
    }


}