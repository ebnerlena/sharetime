package com.lenaebner.sharetime

data class PostData (
    val userId: Int,
    val userName: String,
    val profileImage: String,
    val postImage: String,
    val likes: Int,
    val comments: Int
)