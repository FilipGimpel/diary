package com.gimpel.diary.presentation.navigation

import com.gimpel.diary.presentation.navigation.DiaryDestinationsArgs.DETAIL_ID_ARG
import com.gimpel.diary.presentation.navigation.DiaryScreens.DETAIL_SCREEN
import com.gimpel.diary.presentation.navigation.DiaryScreens.LIST_SCREEN
import com.gimpel.diary.presentation.navigation.DiaryScreens.MAP_SCREEN

object DiaryScreens {
    const val LIST_SCREEN = "list"
    const val MAP_SCREEN = "map"
    const val DETAIL_SCREEN = "detail"
}

object DiaryDestinationsArgs {
    const val DETAIL_ID_ARG = "diaryEntryId"
    const val ADD = "ADD"
}

object DiaryDestinations {
    const val LIST_ROUTE = LIST_SCREEN
    const val MAP_ROUTE = MAP_SCREEN
    const val DETAIL_ROUTE = "$DETAIL_SCREEN/{${DETAIL_ID_ARG}}"
}