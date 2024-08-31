package com.gimpel.diary.domain

import android.content.Context
import com.gimpel.diary.R
import com.gimpel.diary.data.GeocodingRepository
import com.gimpel.diary.data.LocationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val LOCATION_UNKNOWN = R.string.location_unknown

class GetCurrentLocationNameUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    private val locationNameRepository: GeocodingRepository,
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(): Flow<LocationInfo> {
        val lastKnownLocation = locationRepository.getLastKnownLocation()
        return if (lastKnownLocation != null) {
            val (latitude, longitude) = lastKnownLocation
            locationNameRepository.getLocationNameFromCoordinates(latitude, longitude)
                .map { result ->
                    LocationInfo(latitude, longitude, result.getOrDefault(context.getString(R.string.location_unknown)))
                }
        } else {
            flowOf(LocationInfo(0.0,0.0, context.getString(R.string.location_unknown)))
        }
    }
}

data class LocationInfo(
    val latitude: Double,
    val longitude: Double,
    val locationName: String
)