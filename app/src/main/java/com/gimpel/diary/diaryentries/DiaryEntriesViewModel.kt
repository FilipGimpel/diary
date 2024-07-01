package com.gimpel.diary.diaryentries

import androidx.lifecycle.ViewModel
import com.gimpel.diary.data.DiaryEntry
import com.gimpel.diary.data.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * UiState for the task list screen.
 */
data class TasksUiState(
    val entries: List<DiaryEntry> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class DiaryEntriesViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(TasksUiState())
    val uiState = mutableUiState.asStateFlow()
}