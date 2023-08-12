package com.stream_suite.wordgame.ui

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable

@Composable
fun PlayArea(numberOfGames: Int, letters: List<MutableList<Letter>>) {
    val numberOfColumns = when (numberOfGames) {
        1 -> 1
        else -> 2
    }
    LazyVerticalGrid(columns = GridCells.Fixed(numberOfColumns)) {
        items(numberOfGames) {
            GuessBoard(letterRows = letters, gameIndex = it)
        }
    }
}