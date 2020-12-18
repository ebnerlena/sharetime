package com.lenaebner.sharetime

import android.annotation.SuppressLint
import android.util.Log
import android.view.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
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

        private val db = Firebase.firestore
        private var hasLiked: Boolean = false

        @SuppressLint("ClickableViewAccessibility")
        fun bind(post: Post) {

            binding.run {

                db.collection("posts").document(post.documentId).collection("comments").addSnapshotListener { c, _ ->
                    val comments = c?.toObjects<Comment>().orEmpty()
                    commentNr.text = comments?.size.toString()
                }

                postImg.load(post.imageUrl)

                description.text = post.text

                likeNr.text = post.likes.size.toString()
                userName.text = post.author.fullName
                profileImg.load(post.author.profilePicture) {
                    transformations(CircleCropTransformation())
                    fallback(R.drawable.person)
                }

                val userReference = db.collection("people").document(Firebase.auth.currentUser?.uid.toString())

                hasLiked = post.likes.contains(userReference)

               if (hasLiked) {
                    likeImg.load(R.drawable.like_filled)

                } else {
                    likeImg.load(R.drawable.like_unfilled)
                }

                likeImg.setOnClickListener {
                    hasLiked = !hasLiked
                    updatePostLikes(post, hasLiked)
                }

                postImg.setOnTouchListener(object: View.OnTouchListener {
                    val gestureDetector = GestureDetector(object: GestureDetector.SimpleOnGestureListener(){
                        override fun onDoubleTap(e: MotionEvent?): Boolean {
                            hasLiked = !hasLiked
                            updatePostLikes(post, hasLiked)
                            return super.onDoubleTap(e)
                        }
                    })

                    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                        gestureDetector.onTouchEvent(event)
                        return true
                    }
                })

                profileImg.setOnClickListener {
                    it.findNavController().navigate(HomeFragmentDirections.homeToProfile(post.author.fullName, post.author.uid))
                }

                userName.setOnClickListener {
                    it.findNavController().navigate(HomeFragmentDirections.homeToProfile(post.author.fullName, post.author.uid))
                }

                commentImg.setOnClickListener {
                    it.findNavController().navigate(HomeFragmentDirections.homeToComments(post.documentId))
                }
            }
        }


        private fun updatePostLikes(post: Post, hasLiked: Boolean){
            val db = Firebase.firestore

            val userReference = db.collection("people").document(Firebase.auth.currentUser?.uid.toString())

            if(hasLiked) {
                post.likes = post.likes + userReference
                binding.likeImg.load(R.drawable.like_filled)
            }
            else {
                post.likes = post.likes - userReference
                binding.likeImg.load(R.drawable.like_unfilled)
            }
            binding.likeNr.text = post.likes.size.toString()
            db.collection("posts").document(post.documentId).set(post)
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