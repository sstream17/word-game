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

    private lateinit var answer: String
    private var guess = ""

    init {
        resetGame()
    }

    fun setLetters(letter: Char) {
        val currentPosition = _uiState.value.position
        if (currentPosition.col < WORD_LENGTH) {
            val numberOfGames = _uiState.value.numberOfGames
            val letters = _uiState.value.letters
            for (gameIndex in 0 until numberOfGames) {
                setLetter(letter, gameIndex, currentPosition, letters)
            }
            _uiState.update { currentState ->
                currentState.copy(
                    letters = letters, position = currentPosition.nextColumn()
                )
            }
        }
    }

    private fun setLetter(
        letter: Char,
        gameIndex: Int,
        currentPosition: Position,
        letters: List<List<MutableList<Letter>>>
    ) {
        guess += letter
        letters[gameIndex][currentPosition.row][currentPosition.col] = Letter(
            letter = letter, state = LetterState.Initial
        )
    }

    fun deleteLetters() {
        val currentPosition = _uiState.value.position
        if (currentPosition.col > 0) {
            guess = guess.dropLast(1)
            val numberOfGames = _uiState.value.numberOfGames
            val letters = _uiState.value.letters
            for (gameIndex in 0 until numberOfGames) {
                deleteLetter(gameIndex, currentPosition, letters)
            }
            _uiState.update { currentState ->
                currentState.copy(
                    letters = letters, position = currentPosition.previousColumn()
                )
            }
        }
    }

    private fun deleteLetter(
        gameIndex: Int, currentPosition: Position, letters: List<List<MutableList<Letter>>>
    ) {
        letters[gameIndex][currentPosition.row][currentPosition.col - 1] = Letter(
            letter = ' ', state = LetterState.Initial
        )
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
        val (newLetters, newKeys) = getRowColors(answer, guess.lowercase())
        guess = ""
        _uiState.update { currentState ->
            currentState.copy(
                letters = newLetters, keys = newKeys, position = currentState.position.nextRow()
            )
        }
    }

    fun getRowColors(
        answer: String, guess: String
    ): Pair<List<List<MutableList<Letter>>>, MutableMap<Char, MutableList<LetterState>>> {
        val numberOfGames = _uiState.value.numberOfGames
        val currentPosition = _uiState.value.position
        val letters = _uiState.value.letters
        val keys = _uiState.value.keys
        for (gameIndex in 0 until numberOfGames) {
            getRowColor(answer, guess, gameIndex, currentPosition, letters, keys)
        }

        return Pair(letters, keys)
    }

    fun getRowColor(
        answer: String,
        guess: String,
        gameIndex: Int,
        currentPosition: Position,
        letters: List<List<MutableList<Letter>>>,
        keys: MutableMap<Char, MutableList<LetterState>>
    ) {
        letters[gameIndex][currentPosition.row].forEachIndexed { index, _ ->
            val currentLetter = guess[index]
            val currentLetterUpper = currentLetter.uppercaseChar()
            when (currentLetter) {
                answer[index] -> {
                    val state = LetterState.Correct
                    letters[gameIndex][currentPosition.row][index] =
                        letters[gameIndex][currentPosition.row][index].copy(
                            state = state,
                        )
                }

                in answer.filterIndexed { answerLetterIndex, answerLetter -> guess[answerLetterIndex] != answerLetter } -> {
                    val state = LetterState.Exists
                    letters[gameIndex][currentPosition.row][index] =
                        letters[gameIndex][currentPosition.row][index].copy(
                            state = state
                        )
                }

                else -> {
                    val state = LetterState.Missing
                    letters[gameIndex][currentPosition.row][index] =
                        letters[gameIndex][currentPosition.row][index].copy(
                            state = state
                        )
                }
            }
            val letterState = letters[gameIndex][currentPosition.row][index].state
            val keyState = when (keys[currentLetterUpper]?.get(gameIndex)) {
                LetterState.Correct -> LetterState.Correct
                LetterState.Exists -> if (letterState == LetterState.Correct) LetterState.Correct else LetterState.Exists
                else -> letterState
            }
            keys[currentLetterUpper] = mutableListOf(keyState)
        }
    }

    fun resetGame() {
        guess = ""
        answer = getNewWord()
        val numberOfGames = _uiState.value.numberOfGames
        _uiState.update { currentState ->
            currentState.copy(letters = List(numberOfGames) {
                List(numberOfGames + NUMBER_OF_TRIES) {
                    MutableList(
                        WORD_LENGTH
                    ) { Letter(' ') }
                }
            }, keys = Keys.map {
                it to MutableList(1) {
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
