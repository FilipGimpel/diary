package com.gimpel.diary.diaryentries

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.gimpel.diary.BottomNavBar
import com.gimpel.diary.data.DiaryEntry
import com.gimpel.diary.presentation.DiaryScreens.DETAIL_SCREEN
import com.gimpel.diary.toReadable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import java.util.Date

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DiaryEntriesListScreen(
    viewModel: DiaryEntriesViewModel = hiltViewModel(),
    navController: NavController,
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsState()

    val permissionsState = rememberMultiplePermissionsState(permissions =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS,
            )
        } else {
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        }
    )

    // TODO MOVE to separate composables and ask in separate moments

    // before you can ask for background location permission,
    // you need to ask for fine and coarse location permissions
    LaunchedEffect(Unit) {
        if (!permissionsState.allPermissionsGranted) {
            permissionsState.launchMultiplePermissionRequest()
        }
    }

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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(vertical = 16.dp)
        ) {
            items(uiState.value.entries) { diaryEntry ->
                DiaryEntryItem(
                    diaryEntry = diaryEntry,
                    onTaskClick = { navController.navigate("$DETAIL_SCREEN/${diaryEntry.id}") })

            }
        }

    }, floatingActionButton = {
        FloatingActionButton(onClick = {
            navController.navigate("$DETAIL_SCREEN/${"ADD"}")
        }) {
            Icon(Icons.Filled.Add, contentDescription = "Add")
        }
    })
}

@Composable
fun DiaryEntryItem(diaryEntry: DiaryEntry, onTaskClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable { onTaskClick(diaryEntry.id) }

    ) {
        Text(text = diaryEntry.title, style = MaterialTheme.typography.titleMedium)
        Text(text = diaryEntry.locationName, style = MaterialTheme.typography.bodyMedium)
        Text(text = diaryEntry.date.toReadable(), style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview
@Composable
fun DiaryEntryItemPreview() {
    DiaryEntryItem(
        diaryEntry = DiaryEntry(
            title = "Title",
            date = Date(),
            locationName = "Location"
        ),
        onTaskClick = {}
    )
}