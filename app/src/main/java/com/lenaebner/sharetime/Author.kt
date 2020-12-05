package com.lenaebner.sharetime

import com.google.firebase.firestore.PropertyName
data class Author(
        var uid: String = "",
        @JvmField @PropertyName("full_name") var fullName: String = "",
        @JvmField @PropertyName("profile_picture") var profilePicture: String = "",
)