package com.stream_suite.wordgame.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stream_suite.wordgame.GameViewModel

@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel()) {
    val gameUiState by gameViewModel.uiState.collectAsState()

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (header, gameArea, keyboard) = createRefs()
        Box(modifier = Modifier
            .wrapContentSize()
            .constrainAs(header) {
                top.linkTo(parent.top)
                linkTo(start = parent.start, end = parent.end)
            }) {
            GameHeader(onClickRestart = { gameViewModel.resetGame() })
        }
        Box(modifier = Modifier.constrainAs(gameArea) {
            linkTo(top = header.bottom, bottom = keyboard.top)
            linkTo(start = parent.start, end = parent.end)
            height = Dimension.fillToConstraints
        }) {
            GuessBoard(letterRows = gameUiState.letters)
        }
        Box(modifier = Modifier
            .padding(vertical = 16.dp)
            .constrainAs(keyboard) {
                bottom.linkTo(parent.bottom)
                linkTo(start = parent.start, end = parent.end)
            }) {
            Keyboard(keys = gameUiState.keys,
                onClickLetter = { gameViewModel.setLetter(it) },
                onClickDelete = { gameViewModel.deleteLetter() },
                onClickSubmit = { gameViewModel.checkGuess() })
        }
    }
}