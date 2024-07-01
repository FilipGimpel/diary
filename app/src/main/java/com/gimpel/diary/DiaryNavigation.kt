package com.gimpel.diary

import com.gimpel.diary.DiaryDestinationsArgs.DETAIL_ID_ARG
import com.gimpel.diary.DiaryScreens.DETAIL_SCREEN
import com.gimpel.diary.DiaryScreens.LIST_SCREEN

object DiaryScreens {
    const val LIST_SCREEN = "list"
    const val DETAIL_SCREEN = "detail"
}

object DiaryDestinationsArgs {
    const val DETAIL_ID_ARG = "imageId"
}

object DiaryDestinations {
    const val LIST_ROUTE = LIST_SCREEN
    const val DETAIL_ROUTE = "$DETAIL_SCREEN/{${DETAIL_ID_ARG}}"
}