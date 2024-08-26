package com.gimpel.diary.diaryentries

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.gimpel.diary.BottomNavBar
import com.gimpel.diary.data.DiaryEntry
import com.gimpel.diary.presentation.DiaryScreens.DETAIL_SCREEN
import com.gimpel.diary.toReadable
import com.google.type.DateTime
import java.util.Date

@Composable
fun DiaryEntriesListScreen(
    viewModel: DiaryEntriesViewModel = hiltViewModel(),
    navController: NavController,
) {
    val uiState = viewModel.uiState.collectAsState()

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