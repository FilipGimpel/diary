package com.gimpel.diary.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class FirestoreDiaryEntry(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val date: Timestamp = Timestamp.now(),
    val location: GeoPoint = GeoPoint(0.0, 0.0),
    val locationName: String = ""
)