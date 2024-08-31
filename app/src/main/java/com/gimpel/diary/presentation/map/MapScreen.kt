package com.gimpel.diary.presentation.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.gimpel.diary.ui.BottomNavBar
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
    navController: NavController,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
        val backgroundLocationPermission = rememberPermissionState(permission = Manifest.permission.ACCESS_BACKGROUND_LOCATION)

        // once you have fine and coarse location permissions,
        // you can ask for background location permission
        // it will open app settings and ask you to location "ALL TIME"
        LaunchedEffect(Unit) {
            if ((backgroundLocationPermission.status == PermissionStatus.Granted).not() &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            ) {
                backgroundLocationPermission.launchPermissionRequest()
            }
        }
    }

    Scaffold(bottomBar = {
        BottomNavBar(navController = navController)
    }, content = { paddingValues ->

        val cameraPositionState = rememberCameraPositionState()

        LaunchedEffect(uiState.entries) {
            if (uiState.entries.isNotEmpty()) {
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newLatLngBounds(
                        LatLngBounds
                            .builder()
                            .apply {
                                uiState.entries.map { diaryEntry ->
                                    LatLng(diaryEntry.locationLatitude, diaryEntry.locationLongitude)
                                }.forEach { include(it) }
                            }.build(),
                        100 // Padding in pixels
                    )
                )
            }
        }

        GoogleMap(
            modifier = Modifier.padding(paddingValues), cameraPositionState = cameraPositionState
        ) {
            uiState.entries.forEach { entry ->
                Marker(
                    state = MarkerState(position = LatLng(entry.locationLatitude, entry.locationLongitude)),
                    title = entry.title,
                    snippet = entry.date.toReadable()
                )
            }
        }

    })
}

fun Date.toReadable(): String {
    return SimpleDateFormat("dd MMMM yyyy, hh:mm", Locale.getDefault()).format(this)
}
