package com.stream_suite.wordgame.ui

import androidx.compose.ui.graphics.Color
import com.stream_suite.wordgame.LetterState
import com.stream_suite.wordgame.data.WORD_LENGTH
import com.stream_suite.wordgame.ui.theme.OffWhite

const val Keys = "ABCDEFJHIJKLMNOPQRSTUVWXYZ"

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

data class Key(val colors: MutableList<Color> = MutableList(1) { OffWhite })

data class ColorHalves(val leftColor: Color = OffWhite, val rightColor: Color = OffWhite)

data class ColorQuadrants(
    val topLeftColor: Color = OffWhite,
    val topRightColor: Color = OffWhite,
    val bottomLeftColor: Color = OffWhite,
    val bottomRightColor: Color = OffWhite,
)