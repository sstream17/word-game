package com.stream_suite.wordgame

import android.util.Log
import androidx.lifecycle.ViewModel
import com.stream_suite.wordgame.data.WORD_LENGTH
import com.stream_suite.wordgame.data.wordList
import com.stream_suite.wordgame.data.wordListSize
import com.stream_suite.wordgame.ui.GameUiState
import com.stream_suite.wordgame.ui.Keys
import com.stream_suite.wordgame.ui.Letter
import com.stream_suite.wordgame.ui.NUMBER_OF_TRIES
import com.stream_suite.wordgame.ui.Position
import com.stream_suite.wordgame.util.wordlistBinarySearch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Random

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private lateinit var answers: List<String>
    private var guess = ""

    init {
        resetGame()
    }

    fun setLetter(letter: Char) {
        val currentPosition = _uiState.value.position
        if (currentPosition.col < WORD_LENGTH) {
            guess += letter
            val numberOfGames = _uiState.value.numberOfGames
            val letters = _uiState.value.letters
            letters[currentPosition.row][currentPosition.col] =
                Letter(letter = letter, states = MutableList(numberOfGames) { LetterState.Initial })
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
            val numberOfGames = _uiState.value.numberOfGames
            val letters = _uiState.value.letters
            letters[currentPosition.row][currentPosition.col - 1] =
                Letter(letter = ' ', states = MutableList(numberOfGames) { LetterState.Initial })
            _uiState.update { currentState ->
                currentState.copy(
                    letters = letters, position = currentPosition.previousColumn()
                )
            }
        }
    }

    fun checkGuess() {
        val currentPosition = _uiState.value.position
        val maxGuesses = _uiState.value.numberOfGames + NUMBER_OF_TRIES - 1
        when {
            guess.length < WORD_LENGTH -> {
                Log.d("yeet", "checkGuess: not enough chars")
            }

            wordlistBinarySearch(wordList, guess.lowercase(), 0, wordListSize, WORD_LENGTH) -> {
                if (currentPosition.row == maxGuesses) {
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
        val (newLetters, newKeys) = getRowColors(answers, guess.lowercase())
        guess = ""
        _uiState.update { currentState ->
            currentState.copy(
                letters = newLetters, keys = newKeys, position = currentState.position.nextRow()
            )
        }
    }

    fun getRowColors(
        answers: List<String>, guess: String
    ): Pair<List<MutableList<Letter>>, MutableMap<Char, MutableList<LetterState>>> {
        val numberOfGames = _uiState.value.numberOfGames
        val currentPosition = _uiState.value.position
        val letters = _uiState.value.letters
        val keys = _uiState.value.keys
        for (gameIndex in 0 until numberOfGames) {
            if (guess.equals(answers[gameIndex], ignoreCase = true)) {
                Log.d("yeet", "game $gameIndex won")
            }
            getRowColor(answers[gameIndex], guess, gameIndex, currentPosition, letters, keys)
        }

        return Pair(letters, keys)
    }

    fun getRowColor(
        answer: String,
        guess: String,
        gameIndex: Int,
        currentPosition: Position,
        letters: List<MutableList<Letter>>,
        keys: MutableMap<Char, MutableList<LetterState>>
    ) {
        letters[currentPosition.row].forEachIndexed { index, _ ->
            val currentLetter = guess[index]
            val currentLetterUpper = currentLetter.uppercaseChar()
            val newState = when (currentLetter) {
                answer[index] -> LetterState.Correct

                in answer.filterIndexed { answerLetterIndex, answerLetter ->
                    guess[answerLetterIndex] != answerLetter
                } -> LetterState.Exists

                else -> LetterState.Missing

            }
            val newLetter = letters[currentPosition.row][index].copy()
            newLetter.states[gameIndex] = newState
            letters[currentPosition.row][index] = newLetter
            val keyState = when (keys[currentLetterUpper]?.get(gameIndex)) {
                LetterState.Correct -> LetterState.Correct
                LetterState.Exists -> if (newState == LetterState.Correct) LetterState.Correct else LetterState.Exists
                else -> newState
            }
            keys[currentLetterUpper]?.set(gameIndex, keyState)
        }
    }

    fun resetGame() {
        val numberOfGames = _uiState.value.numberOfGames
        guess = ""
        answers = List(numberOfGames) { getNewWord() }
        _uiState.update { currentState ->
            currentState.copy(letters = List(numberOfGames + NUMBER_OF_TRIES) {
                MutableList(WORD_LENGTH) {
                    Letter(' ', MutableList(numberOfGames) { LetterState.Initial })
                }
            }, keys = Keys.map {
                it to MutableList(numberOfGames) {
                    LetterState.Initial
                }
            }.toMap().toMutableMap(), position = currentState.position.reset()
            )
        }
    }

    private fun getNewWord(): String {
        val r = Random(System.nanoTime()).nextInt(wordListSize)
        val wordSlice = r * WORD_LENGTH
        return wordList.slice(wordSlice until wordSlice + WORD_LENGTH)
    }
}
