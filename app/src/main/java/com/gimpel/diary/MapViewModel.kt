package com.gimpel.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gimpel.diary.data.DiaryEntry
import com.gimpel.diary.data.FirestoreRepository
import com.gimpel.diary.detail.LocationRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel  @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(UiState())
    val uiState = mutableUiState.asStateFlow()

    init {
        viewModelScope.launch {
            firestoreRepository.getDiaryEntriesFlow().collect { entries ->
                mutableUiState.value = uiState.value.copy(entries = entries, isLoading = false)
            }
        }
    }
}

data class UiState(
    val entries: List<DiaryEntry> = emptyList(),
    val isLoading: Boolean = true
)