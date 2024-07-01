package com.gimpel.diary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gimpel.diary.data.DiaryEntry
import com.gimpel.diary.diaryentries.DiaryEntriesViewModel

@Composable
fun DiaryNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = DiaryDestinations.LIST_ROUTE,
) {


    NavHost(
        navController = navController, startDestination = startDestination, modifier = modifier
    ) {
        composable(
            DiaryDestinations.LIST_ROUTE,
        ) { entry ->
            DiaryListScreen()


        }
    }
}

@Composable
fun DiaryListScreen(
//    padding: PaddingValues,
//    onItemClicked: Any,
    viewModel: DiaryEntriesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Entries", "Map")

    Scaffold(bottomBar = {
        NavigationBar {
            items.forEachIndexed { index, item ->
                NavigationBarItem(icon = { Icon(Icons.Filled.AccountCircle, contentDescription = item) },
                    label = { Text(item) },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index })
            }
        }
    }, content = { paddingValues ->
        DiaryListContent(entries = emptyList(), onItemClicked = { }, modifier = Modifier.padding(paddingValues))
    }, floatingActionButton = {
        FloatingActionButton(onClick = {
            //navActions.navigateToAddEditTask(0, null)
        }) {
            Icon(Icons.Filled.Add, contentDescription = "Add")
        }
    })
}

@Composable
fun DiaryListContent(
    entries: List<DiaryEntry>, onItemClicked: (String) -> Unit, modifier: Modifier = Modifier
) {
    LazyColumn {
        items(entries) { diaryEntry ->
            DiaryEntryItem(diaryEntry = diaryEntry, onTaskClick = { /*onItemClicked(diaryEntry.id)*/ })
        }
    }
}

@Composable
fun DiaryEntryItem(diaryEntry: DiaryEntry, onTaskClick: Any) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(text = diaryEntry.title, style = MaterialTheme.typography.displayMedium)
        Text(text = diaryEntry.date, style = MaterialTheme.typography.bodyMedium)
        Text(text = diaryEntry.location, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
@Preview
fun DiaryListScreenPreview() {
    Surface {
        DiaryListScreen(viewModel = hiltViewModel())
    }
}

@Composable
@Preview
fun DiaryEntryItemPreview() {
    Surface {
        DiaryEntryItem(diaryEntry = DiaryEntry("Title", "Text", "Date", "Location"), onTaskClick = {})
    }
}