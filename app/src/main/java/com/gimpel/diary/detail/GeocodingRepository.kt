package com.gimpel.diary.detail

import android.location.Address
import android.location.Geocoder
import android.os.Build
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class GeocodingRepository @Inject constructor(
    private val geocoder: Geocoder
) {

    suspend fun getLocationNameFromCoordinates(latitude: Double, longitude: Double): Flow<Result<String>> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            callbackFlow {
                val listener = object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: MutableList<Address>) {
                        if (addresses.isNotEmpty()) {
                            val locationName = addresses[0].getAddressLine(0)
                            launch { send(Result.success(locationName)) }
                        } else {
                            launch { send(Result.failure(IOException("No address found"))) }
                        }
                    }

                    override fun onError(errorMessage: String?) {
                        launch {
                            send(Result.failure(IOException(errorMessage ?: "Geocoding error")))
                        }
                    }
                }

                geocoder.getFromLocationName("$latitude,$longitude", 1, listener)

                awaitClose {
                    /* Flow will throw an exception without this block
                    even if there is nothing to clean up or close */
                }
            }

        } else {
            flow {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)

                if (!addresses.isNullOrEmpty()) {
                    val locationName = addresses[0].getAddressLine(0)
                    emit(Result.success(locationName))
                } else {
                    emit(Result.failure(IOException("No address found")))
                }
            }
        }


}