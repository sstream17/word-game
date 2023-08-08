package com.stream_suite.wordgame

import android.util.Log
import androidx.lifecycle.ViewModel
import com.stream_suite.wordgame.data.WORD_LENGTH
import com.stream_suite.wordgame.data.wordList
import com.stream_suite.wordgame.data.wordListSize
import com.stream_suite.wordgame.ui.GameUiState
import com.stream_suite.wordgame.ui.Letter
import com.stream_suite.wordgame.ui.Position
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
                    letter = letter, state = LetterState.Initial
                )
            _uiState.update { currentState ->
                currentState.copy(
                    letters = letters, position = currentPosition.nextColumn()
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
                    letter = ' ', state = LetterState.Initial
                )
            _uiState.update { currentState ->
                currentState.copy(
                    letters = letters, position = currentPosition.previousColumn()
                )
            }
        }
    }

    fun checkGuess() {
        val currentPosition = _uiState.value.position
        when {
            guess.length < WORD_LENGTH -> {
                Log.d("yeet", "checkGuess: not enough chars")
            }

            guess.equals(answer, ignoreCase = true) -> {
                Log.d("yeet", "checkGuess: won")
                gameWon()
            }

            wordlistBinarySearch(wordList, guess.lowercase(), 0, wordListSize, WORD_LENGTH) -> {
                if (currentPosition.row == 5) {
                    Log.d("yeet", "checkGuess: lost")
                } else {
                    Log.d("yeet", "checkGuess: next try")
                    setNextGuess()
                }
            }

            else -> {
                Log.d("yeet", "checkGuess: not valid word")
            }
        }
    }

    fun gameWon() {
        setNextGuess()
    }

    fun setNextGuess() {
        val newLetters = getRowColor(answer, guess.lowercase())
        guess = ""
        _uiState.update { currentState ->
            currentState.copy(
                letters = newLetters, position = currentState.position.nextRow()
            )
        }
    }

    fun getRowColor(answer: String, guess: String): List<MutableList<Letter>> {
        val currentPosition = _uiState.value.position
        val letters = _uiState.value.letters
        letters[currentPosition.row].forEachIndexed { index, _ ->
            when (guess[index]) {
                answer[index] -> {
                    letters[currentPosition.row][index] = letters[currentPosition.row][index].copy(
                        state = LetterState.Correct
                    )
                }

                in answer.filterIndexed { answerLetterIndex, answerLetter -> guess[answerLetterIndex] != answerLetter } -> {
                    letters[currentPosition.row][index] = letters[currentPosition.row][index].copy(
                        state = LetterState.Exists
                    )
                }

                else -> {
                    letters[currentPosition.row][index] = letters[currentPosition.row][index].copy(
                        state = LetterState.Missing
                    )
                }
            }
        }

        return letters
    }

    fun resetGame() {
        guess = ""
        answer = getNewWord()
        _uiState.update { currentState ->
            currentState.copy(
                letters = List(6) { MutableList(WORD_LENGTH) { Letter(' ') } }, position = Position()
            )
        }
    }

    private fun getNewWord(): String {
        val r = Random(System.nanoTime()).nextInt(wordListSize)
        val wordSlice = r * WORD_LENGTH
        return wordList.slice(wordSlice until wordSlice + WORD_LENGTH)
    }
}
