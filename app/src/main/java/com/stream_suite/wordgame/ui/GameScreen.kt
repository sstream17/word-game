package com.stream_suite.wordgame.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stream_suite.wordgame.GameViewModel

@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel()) {
    val gameUiState by gameViewModel.uiState.collectAsState()

    Column {
        Box(modifier = Modifier.weight(4f)) {
            GuessBoard(letterRows = gameUiState.letters)
        }
        Box(modifier = Modifier.weight(1f)) {
            Keyboard(
                onClickLetter = { gameViewModel.setLetter(it) },
                onClickDelete = { gameViewModel.deleteLetter() },
                onClickSubmit = { gameViewModel.checkGuess() }
            )
        }
    }
}