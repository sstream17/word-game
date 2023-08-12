package com.stream_suite.wordgame.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.stream_suite.wordgame.LetterState
import com.stream_suite.wordgame.R
import com.stream_suite.wordgame.ui.theme.AlmostYellow
import com.stream_suite.wordgame.ui.theme.CorrectGreen
import com.stream_suite.wordgame.ui.theme.InactiveGray
import com.stream_suite.wordgame.ui.theme.OffWhite

@Composable
fun Keyboard(
    keys: MutableMap<Char, MutableList<LetterState>>,
    onClickLetter: (Char) -> Unit,
    onClickDelete: () -> Unit,
    onClickSubmit: () -> Unit,
) {
    val keyGap = dimensionResource(R.dimen.key_gap)
    BoxWithConstraints {
        val keyWidth = (maxWidth - keyGap * 13) / 10
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            KeyRow(keyGap = keyGap) {
                "QWERTYUIOP".forEach { letter ->
                    KeyButton(
                        letter = letter,
                        keyWidth = keyWidth,
                        onClick = onClickLetter,
                        states = keys[letter]
                    )
                }
            }
            KeyRow(keyGap = keyGap) {
                "ASDFGHJKL".forEach { letter ->
                    KeyButton(
                        letter = letter,
                        keyWidth = keyWidth,
                        onClick = onClickLetter,
                        states = keys[letter]
                    )
                }
            }
            KeyRow(keyGap = keyGap) {
                Button(modifier = Modifier.wrapContentWidth(),
                    contentPadding = PaddingValues(4.dp),
                    onClick = { onClickDelete() }) {
                    Icon(
                        painter = painterResource(R.drawable.backspace_24),
                        contentDescription = "Delete"
                    )
                }
                "ZXCVBNM".forEach { letter ->
                    KeyButton(
                        letter = letter,
                        keyWidth = keyWidth,
                        onClick = onClickLetter,
                        states = keys[letter]
                    )
                }
                Button(modifier = Modifier.wrapContentWidth(),
                    contentPadding = PaddingValues(4.dp),
                    onClick = { onClickSubmit() }) {
                    Icon(
                        painter = painterResource(R.drawable.send_24), contentDescription = "Submit"
                    )
                }
            }
        }
    }
}

@Composable
fun KeyRow(keyGap: Dp, content: @Composable () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(keyGap, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
    }
}

@Composable
fun KeyButton(letter: Char, keyWidth: Dp, onClick: (Char) -> Unit, states: List<LetterState>?) {
    when (states?.size) {
        4 -> KeyButton(
            letter = letter, keyWidth = keyWidth, onClick = onClick, states = StateQuadrants(
                topLeftState = states[0],
                topRightState = states[1],
                bottomLeftState = states[2],
                bottomRightState = states[3]
            )
        )

        2 -> KeyButton(
            letter = letter,
            keyWidth = keyWidth,
            onClick = onClick,
            states = StateHalves(leftState = states[0], rightState = states[1])
        )

        1 -> KeyButton(letter = letter, keyWidth = keyWidth, onClick = onClick, state = states[0])
    }
}

@Composable
fun KeyButton(letter: Char, keyWidth: Dp, onClick: (Char) -> Unit, state: LetterState) {
    KeyButton(
        letter = letter,
        keyWidth = keyWidth,
        onClick = onClick,
        states = StateHalves(leftState = state, rightState = state)
    )
}

@Composable
fun KeyButton(letter: Char, keyWidth: Dp, onClick: (Char) -> Unit, states: StateHalves) {
    KeyButton(
        letter = letter, keyWidth = keyWidth, onClick = onClick, states = StateQuadrants(
            topLeftState = states.leftState,
            bottomLeftState = states.leftState,
            topRightState = states.rightState,
            bottomRightState = states.rightState
        )
    )
}

@Composable
fun KeyButton(letter: Char, keyWidth: Dp, onClick: (Char) -> Unit, states: StateQuadrants) {
    ConstraintLayout(modifier = Modifier
        .width(keyWidth)
        .wrapContentHeight()
        .padding(vertical = 4.dp)
        .clip(RoundedCornerShape(4.dp))
        .clickable { onClick(letter) }) {
        val (key, boxOne, boxTwo, boxThree, boxFour) = createRefs()
        val verticalMidpoint = createGuidelineFromStart(0.5f)
        val horizontalMidpoint = createGuidelineFromTop(0.5f)

        Box(modifier = Modifier
            .background(stateToColor(states.topLeftState))
            .constrainAs(boxOne) {
                linkTo(top = parent.top, bottom = horizontalMidpoint)
                linkTo(start = parent.start, end = verticalMidpoint)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })
        Box(modifier = Modifier
            .background(stateToColor(states.topRightState))
            .constrainAs(boxTwo) {
                linkTo(top = parent.top, bottom = horizontalMidpoint)
                linkTo(start = verticalMidpoint, end = parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })
        Box(modifier = Modifier
            .background(stateToColor(states.bottomLeftState))
            .constrainAs(boxThree) {
                linkTo(top = horizontalMidpoint, bottom = parent.bottom)
                linkTo(start = parent.start, end = verticalMidpoint)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })
        Box(modifier = Modifier
            .background(stateToColor(states.bottomRightState))
            .constrainAs(boxFour) {
                linkTo(top = horizontalMidpoint, bottom = parent.bottom)
                linkTo(start = verticalMidpoint, end = parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })
        Text(text = letter.toString(), modifier = Modifier
            .constrainAs(key) {
                centerTo(parent)
            }
            .padding(4.dp, 12.dp))
    }
}

private fun stateToColor(state: LetterState): Color {
    return when (state) {
        LetterState.Initial -> OffWhite
        LetterState.Correct -> CorrectGreen
        LetterState.Exists -> AlmostYellow
        LetterState.Missing -> InactiveGray
    }
}

@Preview(showBackground = true)
@Composable
fun KeyboardPreview() {
    Keyboard(onClickLetter = {},
        onClickDelete = {},
        onClickSubmit = {},
        keys = Keys.map { it to mutableListOf(LetterState.Initial) }.toMap().toMutableMap()
    )
}

@Preview(showBackground = true)
@Composable
fun KeyboardTwoColorPreview() {
    Keyboard(onClickLetter = {}, onClickDelete = {}, onClickSubmit = {}, keys = Keys.map {
        it to mutableListOf(LetterState.Correct, LetterState.Exists)
    }.toMap().toMutableMap())
}

@Preview(showBackground = true)
@Composable
fun KeyboardFourColorPreview() {
    Keyboard(onClickLetter = {}, onClickDelete = {}, onClickSubmit = {}, keys = Keys.map {
        it to mutableListOf(
            LetterState.Correct, LetterState.Exists, LetterState.Missing, LetterState.Initial
        )
    }.toMap().toMutableMap())
}