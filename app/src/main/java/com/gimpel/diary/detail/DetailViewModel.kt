package com.gimpel.diary.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gimpel.diary.data.DiaryEntry
import com.gimpel.diary.data.FirestoreRepository
import com.gimpel.diary.presentation.DiaryDestinationsArgs.ADD
import com.gimpel.diary.presentation.DiaryDestinationsArgs.DETAIL_ID_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getLocationNameUseCase: GetCurrentLocationNameUseCase,
    private val getLocationFromCoordinatesUseCase: GetLocationFromCoordinatesUseCase,
    private val diaryEntryRepository: FirestoreRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mutableUiState =
        MutableStateFlow(
            UiState(
                mode = savedStateHandle.get<String>(DETAIL_ID_ARG)?.let {
                    editedId -> if (editedId == ADD) Mode.Add else Mode.Edit(editedId)
        } ?: Mode.Add // Default to Mode.Add if no ID is provided
        ))
    val uiState = mutableUiState.asStateFlow()

    init {
        if (mutableUiState.value.mode is Mode.Edit) {
            val id = (mutableUiState.value.mode as Mode.Edit).id
            viewModelScope.launch {
                val diaryEntry = diaryEntryRepository.getDiaryEntry(id)
                mutableUiState.value = mutableUiState.value.copy(
                    title = diaryEntry.title,
                    content = diaryEntry.content,
                    locationLatitude = diaryEntry.locationLatitude,
                    locationLongitude = diaryEntry.locationLongitude,
                    locationName = diaryEntry.locationName
                )

                if (diaryEntry.locationName.isEmpty()) {
                    getLocationFromCoordinatesUseCase(diaryEntry.locationLatitude, diaryEntry.locationLongitude).collect { locationName ->
                        mutableUiState.value = mutableUiState.value.copy(locationName = locationName)
                    }
                }
            }
        }
    }

    // this will be launched from the UI once we know we have the permissions/ask for them
    fun fetchLocation() {
        viewModelScope.launch {
            if (uiState.value.mode is Mode.Add) {
                getLocationNameUseCase().collect { location ->
                    mutableUiState.value =
                        mutableUiState.value.copy(
                            locationLatitude = location.latitude,
                            locationLongitude = location.longitude,
                            locationName = location.locationName
                        )
                }
            } else {
                getLocationFromCoordinatesUseCase(uiState.value.locationLatitude, uiState.value.locationLongitude).collect { locationName ->
                    mutableUiState.value = mutableUiState.value.copy(locationName = locationName)
                }
            }
        }
    }

    fun onTitleChange(newText: String) {
        mutableUiState.value = mutableUiState.value.copy(title = newText)
    }

    fun onContentChange(newText: String) {
        mutableUiState.value = mutableUiState.value.copy(content = newText)
    }

    fun saveCurrentEntry() {
        viewModelScope.launch {
            val diaryEntry = DiaryEntry(
                id = if (uiState.value.mode is Mode.Edit) (uiState.value.mode as Mode.Edit).id else "",
                title = uiState.value.title,
                content = uiState.value.content,
                locationLatitude = uiState.value.locationLatitude,
                locationLongitude = uiState.value.locationLongitude,
                locationName = uiState.value.locationName
            )

            val result = diaryEntryRepository.addOrUpdateDiaryEntry(diaryEntry)
            if (result.isSuccess) {
                Log.d("DetailViewModel", "Successfully saved entry")
            } else {
                Log.d("DetailViewModel", "Failed to save entry")
            }
        }
    }
}

sealed class Mode {
    data class Edit(val id: String) : Mode()
    data object Add : Mode()
}

data class UiState( // todo make uistate a sealed class of type edit/add
    val mode: Mode = Mode.Add,
    val title: String = "",
    val content: String = "",
    val locationLatitude: Double = 0.0,
    val locationLongitude: Double = 0.0,
    val locationName: String = ""
)

