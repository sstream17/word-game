package com.stream_suite.wordgame

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.stream_suite.wordgame.data.AppResourceProvider
import com.stream_suite.wordgame.ui.GameScreen
import com.stream_suite.wordgame.ui.Keys
import com.stream_suite.wordgame.ui.theme.WordGameTheme

class MainActivity : ComponentActivity() {
    private val viewModel: GameViewModel by viewModels()
    private val keyboardSubmitFocusRequester = FocusRequester()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppResourceProvider.initialize(applicationContext)

        setContent {
            WordGameTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    // This invisible button is used to submit a guess with a hardware keyboard
                    Button(
                        modifier = Modifier
                            .alpha(0f)
                            .focusRequester(keyboardSubmitFocusRequester),
                        onClick = { viewModel.checkGuess() },
                    ) {}
                    GameScreen(viewModel)
                }
            }
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        keyboardSubmitFocusRequester.requestFocus()
        val eventChar = event?.unicodeChar?.toChar()?.uppercaseChar()
        when {
            keyCode == KeyEvent.KEYCODE_DEL -> {
                viewModel.deleteLetter()
                return true
            }

            eventChar != null && eventChar in Keys -> {
                viewModel.setLetter(event.unicodeChar.toChar())
                return true
            }

            else -> Unit
        }
        return super.onKeyUp(keyCode, event)
    }
}
