package com.lenaebner.sharetime

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName
data class Person(
        @JvmField @PropertyName("full_name") var fullName: String = "",
        @JvmField @PropertyName("profile_picture") var profilePicture: String
        = "",
        var following: List<DocumentReference> = emptyList(),
        var posts: List<DocumentReference> = emptyList(),
        var followers: List<DocumentReference> = emptyList()

)