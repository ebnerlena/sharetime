package com.lenaebner.sharetime

import com.google.firebase.Timestamp

data class Comment (
    var author: Author = Author(),
    var text: String = "",
    var timestamp: Timestamp = Timestamp.now()
)