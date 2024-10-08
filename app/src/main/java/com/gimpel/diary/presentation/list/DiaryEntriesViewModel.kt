package com.gimpel.diary.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gimpel.diary.data.model.DiaryEntry
import com.gimpel.diary.data.FirestoreRepository
import com.gimpel.diary.domain.AddGeofenceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DiaryEntriesViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val addGeofenceUseCase: AddGeofenceUseCase
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(UiState())
    val uiState = mutableUiState.asStateFlow()

    init {
        viewModelScope.launch {
            firestoreRepository.getDiaryEntriesFlow().collect { entries ->
                if (entries.isEmpty()) {
                    populate()
                } else {
                    mutableUiState.value = uiState.value.copy(entries = entries, isLoading = false)

                    addGeoFences(entries)
                }
            }
        }
    }

    fun addGeoFences(entries: List<DiaryEntry>) {
        addGeofenceUseCase(entries)
    }

    suspend fun populate() {
        val entries = listOf(
            DiaryEntry(
                title = "Exploring the Old Town",
                content = "Today, I wandered through the charming cobblestone streets of Warsaw's Old Town. ",
                locationLatitude = 52.247256,
                locationLongitude = 21.013998,
                date = createDate(2024, 8, 1),
                locationName = "plac Zamkowy 4, 00-277 Warszawa, Polska",
                imageUri = "https://firebasestorage.googleapis.com/v0/b/diary-10c59.appspot.com/o/buildin%2Fmiasto.png?alt=media&token=d7f283be-6612-44a9-aa20-fe63cf836775"
            ),
            DiaryEntry(
                title = "Reflecting at the Warsaw Uprising Museum",
                content = "This is the second entry",
                locationLatitude = 52.232889,
                locationLongitude = 20.980442,
                date = createDate(2024, 8, 2),
                locationName = "Muzeum Powstania Warszawskiego, 00-844 Warszawa, Polska",
                imageUri = "https://firebasestorage.googleapis.com/v0/b/diary-10c59.appspot.com/o/buildin%2Fpowstanie.png?alt=media&token=ec598f4c-801f-4320-8268-3fd9fc6ba102"
            ),
            DiaryEntry(
                title = "A Peaceful Stroll in Łazienki Park",
                content = "The park's lush greenery and serene lakes provided a perfect escape from the city's hustle.",
                locationLatitude = 52.212777,
                locationLongitude = 21.035945,
                date = createDate(2024, 8, 3),
                locationName = "Aleje Ujazdowskie 6, 00-001 Warszawa, Polska",
                imageUri = "https://firebasestorage.googleapis.com/v0/b/diary-10c59.appspot.com/o/buildin%2Flazienki.png?alt=media&token=70606cb7-21ca-4253-bc42-292d8c99a883"
            ),
            DiaryEntry(
                title = "Discovering the Neon Museum",
                content = "Warsaw's Neon Museum was an unexpected delight.",
                locationLatitude = 52.244086,
                locationLongitude = 21.047574,
                date = createDate(2024, 8, 4),
                locationName = "aleja Zieleniecka 6/8, 03-727 Warszawa, Polska",
                imageUri = "https://firebasestorage.googleapis.com/v0/b/diary-10c59.appspot.com/o/buildin%2Fneon.png?alt=media&token=8231e155-c713-493d-949d-15d77d260a9b"
            ),
            DiaryEntry(
                title = "Evening at the Palace of Culture and Science",
                content = "This is the third entry",
                locationLatitude = 52.231838,
                locationLongitude = 21.006724,
                date = createDate(2024, 8, 5),
                locationName = "plac Defilad 1, 00-901 Warszawa, Polska",
                imageUri = "https://firebasestorage.googleapis.com/v0/b/diary-10c59.appspot.com/o/buildin%2Fpalac.png?alt=media&token=7e4f9d43-422e-4b3c-8cc4-329a4be0b202"
            )
        )

        firestoreRepository.addDiaryEntries(entries)
    }
}

fun createDate(year: Int, month: Int, day: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month - 1) // Adjust for 0-indexed months
    calendar.set(Calendar.DAY_OF_MONTH, day)
    return calendar.time
}

data class UiState(
    val entries: List<DiaryEntry> = emptyList(),
    val isLoading: Boolean = true
)
