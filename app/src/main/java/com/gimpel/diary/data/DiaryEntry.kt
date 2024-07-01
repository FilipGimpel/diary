package com.gimpel.diary.data

import android.location.Location

data class DiaryEntry(
    val title: String,
    val text: String,
    val date: String,
    val location: String
) {
}