package com.stream_suite.wordgame.ui

import com.stream_suite.wordgame.LetterState

data class GameUiState(
    val letters: List<MutableList<Letter>> = List(6) { MutableList(5) { Letter(' ') } },
    val position: Position = Position(0, 0),
)

data class Letter(
    val letter: Char,
    val state: LetterState = LetterState.Initial,
)

data class Position(var row: Int, var col: Int) {
    fun nextColumn(): Position {
        return Position(row, col + 1)
    }

    fun previousColumn(): Position {
        return Position(row, col - 1)
    }

    fun nextRow(): Position {
        return Position(row + 1, 0)
    }

    fun reset(): Position {
        return Position(0, 0)
    }
}

data class Key(val backgroundColor: Int, val textColor: Int)