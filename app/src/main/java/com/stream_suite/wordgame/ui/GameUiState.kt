package com.stream_suite.wordgame.ui

import com.stream_suite.wordgame.LetterState
import com.stream_suite.wordgame.data.WORD_LENGTH

const val Keys = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

data class GameUiState(
    val letters: List<MutableList<Letter>> = List(6) { MutableList(WORD_LENGTH) { Letter(' ') } },
    val keys: MutableMap<Char, Key> = Keys.map { it to Key() }.toMap().toMutableMap(),
    val position: Position = Position(),
)

data class Letter(
    val letter: Char,
    val state: LetterState = LetterState.Initial,
)

data class Position(var row: Int = 0, var col: Int = 0) {
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

data class Key(val states: MutableList<LetterState> = MutableList(1) { LetterState.Initial })

data class StateHalves(val leftState: LetterState = LetterState.Initial, val rightState: LetterState = LetterState.Initial)

data class StateQuadrants(
    val topLeftState: LetterState = LetterState.Initial,
    val topRightState: LetterState = LetterState.Initial,
    val bottomLeftState: LetterState = LetterState.Initial,
    val bottomRightState: LetterState = LetterState.Initial,
)