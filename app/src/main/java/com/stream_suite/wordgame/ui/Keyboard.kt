package com.stream_suite.wordgame.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stream_suite.wordgame.R

@Composable
fun Keyboard(onClickLetter: (Char) -> Unit, onClickDelete: () -> Unit, onClickSubmit: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        KeyRow {
            "QWERTYUIOP".forEach { letter ->
                KeyButton(letter = letter, onClick = onClickLetter)
            }
        }
        KeyRow {
            "ASDFGHJKL".forEach { letter ->
                KeyButton(letter = letter, onClick = onClickLetter)
            }
        }
        KeyRow {
            Button(
                modifier = Modifier
                    .wrapContentWidth(),
                contentPadding = PaddingValues(4.dp),
                onClick = { onClickDelete() }) {
                Icon(
                    painter = painterResource(R.drawable.backspace_24),
                    contentDescription = "Delete"
                )
            }
            "ZXCVBNM".forEach { letter ->
                KeyButton(letter = letter, onClick = onClickLetter)
            }
            Button(
                modifier = Modifier
                    .wrapContentWidth(),
                contentPadding = PaddingValues(4.dp),
                onClick = { onClickSubmit() }) {
                Icon(painter = painterResource(R.drawable.send_24), contentDescription = "Submit")
            }
        }
    }
}

@Composable
fun KeyRow(content: @Composable () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
    ) {
        content()
    }
}

@Composable
fun KeyButton(letter: Char, onClick: (Char) -> Unit) {
    Button(
        modifier = Modifier
            .wrapContentWidth()
            .width(32.dp),
        contentPadding = PaddingValues(4.dp),
        onClick = { onClick(letter) }) {
        Text(
            text = letter.toString()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KeyboardPreview() {
    Keyboard(onClickLetter = {}, onClickDelete = {}, onClickSubmit = {})
}