package com.gimpel.diary.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultDiaryRepository @Inject constructor() : DiaryRepository {
    override fun getDiaryEntryStream(): Flow<List<DiaryEntry>> {
        return flow {
            // Fetch the data from your data source here
            // For now, let's just emit some dummy data
            emit(listOf(
                DiaryEntry("Title 1", "Content 1", "2021-01-01", "Location 1"),
                DiaryEntry("Title 2", "Content 2", "2021-01-02", "Location 2"),
                DiaryEntry("Title 3", "Content 3", "2021-01-03", "Location 3"),
                DiaryEntry("Title 4", "Content 4", "2021-01-04", "Location 4"),
                DiaryEntry("Title 5", "Content 5", "2021-01-05", "Location 5"),
                DiaryEntry("Title 6", "Content 6", "2021-01-06", "Location 6"),
            ))
        }
    }
}