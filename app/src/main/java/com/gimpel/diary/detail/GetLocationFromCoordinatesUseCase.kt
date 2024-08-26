package com.gimpel.diary.detail

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLocationFromCoordinatesUseCase @Inject constructor(
    private val locationNameRepository: GeocodingRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double): Flow<String> {
        return locationNameRepository.getLocationNameFromCoordinates(latitude, longitude)
            .map { result ->
                result.getOrDefault("Location Unknown")
            }
    }
}