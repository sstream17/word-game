package com.stream_suite.wordgame

import android.util.Log
import androidx.lifecycle.ViewModel
import com.stream_suite.wordgame.data.WORD_LENGTH
import com.stream_suite.wordgame.data.wordList
import com.stream_suite.wordgame.data.wordListSize
import com.stream_suite.wordgame.ui.GameUiState
import com.stream_suite.wordgame.util.wordlistBinarySearch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Random

const val NUMBER_OF_TRIES = 5

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private lateinit var answer: String
    private var guess = ""

    init {
        resetGame()
    }

    fun setLetter(letter: Char) {
        val currentPosition = _uiState.value.position
        if (currentPosition.col < WORD_LENGTH) {
            guess += letter
            val letters = _uiState.value.letters
            letters[currentPosition.row][currentPosition.col] =
                letters[currentPosition.row][currentPosition.col].copy(
                    letter = letter,
                    state = LetterState.Initial
                )
            _uiState.update { currentState ->
                currentState.copy(
                    letters = letters,
                    position = currentPosition.nextColumn()
                )
            }
        }
    }

    fun deleteLetter() {
        val currentPosition = _uiState.value.position
        if (currentPosition.col > 0) {
            guess = guess.dropLast(1)
            val letters = _uiState.value.letters
            letters[currentPosition.row][currentPosition.col - 1] =
                letters[currentPosition.row][currentPosition.col - 1].copy(
                    letter = ' ',
                    state = LetterState.Initial
                )
            _uiState.update { currentState ->
                currentState.copy(
                    letters = letters,
                    position = currentPosition.previousColumn()
                )
            }
        }
    }

    fun checkGuess() {
        val currentPosition = _uiState.value.position
        when {
            guess.length < 5 -> {
                Log.d("yeet", "checkGuess: not enough chars")
            }
            guess.equals(answer, ignoreCase = true) -> {
                Log.d("yeet", "checkGuess: won")
            }
            wordlistBinarySearch(wordList, guess.lowercase(), 0, wordListSize, WORD_LENGTH) -> {
                if (currentPosition.row == 5) {
                    Log.d("yeet", "checkGuess: lost")
                } else {
                    Log.d("yeet", "checkGuess: next try")
                    guess = ""
                    _uiState.update { currentState ->
                        currentState.copy(
                            position = currentState.position.nextRow()
                        )
                    }
                }
            }
            else -> {
                Log.d("yeet", "checkGuess: not valid word")
            }
        }
    }

    fun resetGame() {
        answer = getNewWord()
    }

    private fun getNewWord(): String {
        val r = Random(System.nanoTime()).nextInt(wordListSize)
        val wordSlice = r * WORD_LENGTH
        return wordList.slice(wordSlice until wordSlice + WORD_LENGTH)
    }
}
