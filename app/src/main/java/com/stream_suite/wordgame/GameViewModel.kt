package com.stream_suite.wordgame

import android.util.Log
import androidx.lifecycle.ViewModel
import com.stream_suite.wordgame.data.WORD_LENGTH
import com.stream_suite.wordgame.data.wordList
import com.stream_suite.wordgame.data.wordListSize
import com.stream_suite.wordgame.ui.GameUiState
import com.stream_suite.wordgame.ui.Keys
import com.stream_suite.wordgame.ui.Letter
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
            letters[currentPosition.row][currentPosition.col] = Letter(
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
            letters[currentPosition.row][currentPosition.col - 1] = Letter(
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
        val (newLetters, newKeys) = getRowColor(answer, guess.lowercase())
        guess = ""
        _uiState.update { currentState ->
            currentState.copy(
                letters = newLetters, keys = newKeys, position = currentState.position.nextRow()
            )
        }
    }

    fun getRowColor(
        answer: String, guess: String
    ): Pair<List<MutableList<Letter>>, MutableMap<Char, MutableList<LetterState>>> {
        val currentPosition = _uiState.value.position
        val letters = _uiState.value.letters
        val keys = _uiState.value.keys
        letters[currentPosition.row].forEachIndexed { index, _ ->
            val currentLetter = guess[index]
            val currentLetterUpper = currentLetter.uppercaseChar()
            when (currentLetter) {
                answer[index] -> {
                    val state = LetterState.Correct
                    letters[currentPosition.row][index] = letters[currentPosition.row][index].copy(
                        state = state,
                    )
                }

                in answer.filterIndexed { answerLetterIndex, answerLetter -> guess[answerLetterIndex] != answerLetter } -> {
                    val state = LetterState.Exists
                    letters[currentPosition.row][index] = letters[currentPosition.row][index].copy(
                        state = state
                    )
                }

                else -> {
                    val state = LetterState.Missing
                    letters[currentPosition.row][index] = letters[currentPosition.row][index].copy(
                        state = state
                    )
                }
            }
            val letterState = letters[currentPosition.row][index].state
            val keyState = when (keys[currentLetterUpper]?.get(0)) {
                LetterState.Correct -> LetterState.Correct
                LetterState.Exists -> if (letterState == LetterState.Correct) LetterState.Correct else LetterState.Exists
                else -> letterState
            }
            keys[currentLetterUpper] = mutableListOf(keyState)
        }

        return Pair(letters, keys)
    }

    fun resetGame() {
        guess = ""
        answer = getNewWord()
        _uiState.update { currentState ->
            currentState.copy(letters = List(6) { MutableList(WORD_LENGTH) { Letter(' ') } },
                keys = Keys.map {
                    it to MutableList(1) {
                        LetterState.Initial
                    }
                }.toMap().toMutableMap(),
                position = currentState.position.reset()
            )
        }
    }

    private fun getNewWord(): String {
        val r = Random(System.nanoTime()).nextInt(wordListSize)
        val wordSlice = r * WORD_LENGTH
        return wordList.slice(wordSlice until wordSlice + WORD_LENGTH)
    }
}
