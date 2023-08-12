package com.stream_suite.wordgame.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stream_suite.wordgame.GameViewModel

@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel()) {
    val gameUiState by gameViewModel.uiState.collectAsState()

    Column(verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)) {
        Box(modifier = Modifier.wrapContentSize()) {
            GameHeader(onClickRestart = { gameViewModel.resetGame() })
        }
        Box(modifier = Modifier.weight(7f)) {
            GuessBoard(letterRows = gameUiState.letters)
        }
        Box(
            modifier = Modifier
                .weight(2f)
                .padding(vertical = 16.dp)
        ) {
            Keyboard(
                keys = gameUiState.keys,
                onClickLetter = { gameViewModel.setLetter(it) },
                onClickDelete = { gameViewModel.deleteLetter() },
                onClickSubmit = { gameViewModel.checkGuess() }
            )
        }
    }
}