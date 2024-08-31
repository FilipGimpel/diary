package com.gimpel.diary.domain

import android.content.Context
import com.gimpel.diary.R
import com.gimpel.diary.data.GeocodingRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLocationFromCoordinatesUseCase @Inject constructor(
    private val locationNameRepository: GeocodingRepository,
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(latitude: Double, longitude: Double): Flow<String> {
        return locationNameRepository.getLocationNameFromCoordinates(latitude, longitude)
            .map { result ->
                result.getOrDefault(context.getString(R.string.location_unknown))
            }
    }
}