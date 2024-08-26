package com.gimpel.diary.detail

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.gimpel.diary.R
import com.gimpel.diary.data.DiaryEntry

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
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailContent(
    uiState: UiState,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
) {

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
                label = { Text(text = "Enter your diary entry title") },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = uiState.content,
                onValueChange = onContentChange,
                label = { Text(text = "Enter your diary entry content") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Icon(Icons.Filled.LocationOn, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                Text(text = "Location: ${uiState.locationName}")
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
        onContentChange = { }
    )
}
