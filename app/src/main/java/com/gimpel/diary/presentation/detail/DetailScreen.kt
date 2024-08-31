package com.gimpel.diary.presentation.detail

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.gimpel.diary.R

@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val locationPermissionRequest = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            viewModel.fetchLocation()
        }
    }

    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.fetchLocation()
            }

            else -> {
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    DetailContent(
        uiState,
        onTitleChange = { viewModel.onTitleChange(it) },
        onContentChange = { viewModel.onContentChange(it) },
        onBackClick = { navController.popBackStack() },
        onSaveClick = {
            viewModel.saveCurrentEntry()
            navController.popBackStack()
        },
        onImageCaptured = { bitmap ->
            viewModel.uploadBitmap(bitmap)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailContent(
    uiState: UiState,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onImageCaptured: (Bitmap) -> Unit
) {
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        onImageCaptured(bitmap!!)
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = stringResource(R.string.label_edit))
        }, navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.content_description_back)
                )
            }
        }, actions = {
            IconButton(onClick = onSaveClick) {

                Icon(
                    Icons.Filled.Save,
                    contentDescription = stringResource(R.string.content_description_save)
                )
            }
        })
    }, content = { paddingValues ->


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)

        ) {
            OutlinedTextField(value = uiState.title,
                onValueChange =  onTitleChange,
                label = { Text(text = stringResource(R.string.diary_entry_title_hint)) },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = uiState.content,
                onValueChange = onContentChange,
                label = { Text(text = stringResource(R.string.diary_entry_content_hint)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(9f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween, // Distribute space between items
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    modifier = Modifier.weight(1f), // Allow image to occupy available space
                    painter = rememberAsyncImagePainter(model = uiState.imageUri),
                    contentDescription = stringResource(R.string.captured_image)
                )

                Button(onClick = { takePictureLauncher.launch() }) {
                    if ( uiState.imageUri.isEmpty()) Text(stringResource(R.string.take_picture)) else Text(
                        stringResource(R.string.retake_picture)
                    )
                } // todo delete picture button
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Icon(Icons.Filled.LocationOn, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                Text(text = stringResource(R.string.location, uiState.locationName))
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // todo: photo, recording,
    })
}

@Preview
@Composable
fun PreviewDetailScreen() {
    DetailContent(
        UiState(
            title = "Title",
            content = "Content",
            locationName = "Unknown Road, Mountain View, CA, 94043, United States of America"
        ),
        onBackClick = { },
        onSaveClick = { },
        onTitleChange = { },
        onContentChange = { },
        onImageCaptured = { }
    )
}
