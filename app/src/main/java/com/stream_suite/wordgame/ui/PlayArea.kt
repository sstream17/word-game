package com.stream_suite.wordgame.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PlayArea(numberOfGames: Int, letters: List<MutableList<Letter>>) {
    val numberOfColumns = when (numberOfGames) {
        1 -> 1
        else -> 2
    }
    val numberOfRows = numberOfGames / numberOfColumns
    BoxWithConstraints {
        val boardHeight = maxHeight / numberOfRows
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(numberOfColumns),
            userScrollEnabled = false
        ) {
            items(numberOfGames) {
                GuessBoard(letterRows = letters, gameIndex = it, height = boardHeight)
            }
        }
    }
}