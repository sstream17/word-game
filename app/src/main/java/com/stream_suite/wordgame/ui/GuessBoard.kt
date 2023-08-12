package com.stream_suite.wordgame.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

@Composable
fun GuessBoard(letterRows: List<List<Letter>>, gameIndex: Int) {
    Column {
        letterRows.forEach { row -> GuessRow(letters = row, gameIndex = gameIndex) }
    }
}