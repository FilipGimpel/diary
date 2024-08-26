package com.gimpel.diary.detail

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCurrentLocationNameUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    private val locationNameRepository: GeocodingRepository
) {

    // todo error handling
    suspend operator fun invoke(): Flow<LocationInfo> {
        val lastKnownLocation = locationRepository.getLastKnownLocation()
        return if (lastKnownLocation != null) {
            val (latitude, longitude) = lastKnownLocation
            locationNameRepository.getLocationNameFromCoordinates(latitude, longitude)
                .map { result -> // todo have a common "Location Uknown" constant used in both usecases
                    LocationInfo(latitude, longitude, result.getOrDefault("Location Unknown"))
                }
        } else {
            flowOf(LocationInfo(0.0,0.0, "Location Unknown"))
        }
    }
}

data class LocationInfo(
    val latitude: Double,
    val longitude: Double,
    val locationName: String
)