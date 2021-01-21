package com.lenaebner.sharetime.firestore

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

fun FirebaseFirestore.users() = collection("people")
fun FirebaseFirestore.user(id: String) = users().document(id)
fun FirebaseFirestore.currentUser() = user(Firebase.auth.uid.toString())
fun FirebaseFirestore.allPosts() = collection("posts")
fun FirebaseFirestore.post(id: String) = allPosts().document(id)
fun FirebaseFirestore.postComments(id: String) = post(id).collection("comments")
