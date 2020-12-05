package com.lenaebner.sharetime

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
data class Post(
        @JvmField @PropertyName("image_url") var imageUrl: String = "",
        var author: Author = Author(),
        var text: String = "",
        var timestamp: Timestamp = Timestamp.now()
)