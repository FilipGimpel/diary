package com.gimpel.diary.data

import kotlinx.coroutines.flow.Flow

interface DiaryRepository {
    fun getDiaryEntryStream(): Flow<List<DiaryEntry>>
}