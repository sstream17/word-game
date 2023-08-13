package com.stream_suite.wordgame.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun GuessBoard(letterRows: List<List<Letter>>, gameIndex: Int, height: Dp) {
    Column(
        modifier = Modifier.height(height),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        letterRows.forEach { row -> GuessRow(letters = row, gameIndex = gameIndex) }
    }
}