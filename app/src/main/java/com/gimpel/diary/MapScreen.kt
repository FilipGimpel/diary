package com.gimpel.diary

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.gimpel.diary.presentation.DiaryScreens.DETAIL_SCREEN
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsState()

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

// todo move it somewhere reasonable
fun Date.toReadable(): String {
    return SimpleDateFormat("dd MMMM yyyy, hh:mm", Locale.getDefault()).format(this)
}
