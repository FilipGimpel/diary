package com.gimpel.diary.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import java.util.Date

data class DiaryEntry(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val date: Date = Date(),
    val locationLatitude: Double = 0.0,
    val locationLongitude: Double = 0.0,
    val locationName: String = "",
    val imageUri: String = "",
)

//todo move to separate
fun DiaryEntry.toFirestoreDiaryEntry(id: String = ""): FirestoreDiaryEntry {
    return FirestoreDiaryEntry(
        id = id.ifEmpty { this.id },
        title = this.title,
        content = this.content,
        date = Timestamp(this.date),
        location = GeoPoint(this.locationLatitude, this.locationLongitude),
        locationName = this.locationName,
        imageUri = this.imageUri
    )
}

fun FirestoreDiaryEntry.toDiaryEntry(): DiaryEntry {
    return DiaryEntry(
        id = this.id,
        title = this.title,
        content = this.content,
        date = this.date.toDate(),
        locationLatitude = this.location.latitude,
        locationLongitude = this.location.longitude,
        locationName = this.locationName,
        imageUri = this.imageUri
    )
}