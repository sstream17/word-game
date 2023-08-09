package com.stream_suite.wordgame.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.stream_suite.wordgame.R
import com.stream_suite.wordgame.ui.theme.OffWhite

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
            Button(modifier = Modifier.wrapContentWidth(),
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
            Button(modifier = Modifier.wrapContentWidth(),
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
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
    }
}

@Composable
fun KeyButton(letter: Char, onClick: (Char) -> Unit) {
    ConstraintLayout(modifier = Modifier
        .width(32.dp)
        .wrapContentHeight()
        .padding(vertical = 4.dp)
        .clip(RoundedCornerShape(4.dp))
        .clickable { onClick(letter) }) {
        val (key, boxOne, boxTwo, boxThree, boxFour) = createRefs()
        val verticalMidpoint = createGuidelineFromStart(0.5f)
        val horizontalMidpoint = createGuidelineFromTop(0.5f)

        Box(modifier = Modifier
            .background(OffWhite)
            .constrainAs(boxOne) {
                linkTo(top = parent.top, bottom = horizontalMidpoint)
                linkTo(start = parent.start, end = verticalMidpoint)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })
        Box(modifier = Modifier
            .background(OffWhite)
            .constrainAs(boxTwo) {
                linkTo(top = parent.top, bottom = horizontalMidpoint)
                linkTo(start = verticalMidpoint, end = parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })
        Box(modifier = Modifier
            .background(OffWhite)
            .constrainAs(boxThree) {
                linkTo(top = horizontalMidpoint, bottom = parent.bottom)
                linkTo(start = parent.start, end = verticalMidpoint)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })
        Box(modifier = Modifier
            .background(OffWhite)
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

@Preview(showBackground = true)
@Composable
fun KeyboardPreview() {
    Keyboard(onClickLetter = {}, onClickDelete = {}, onClickSubmit = {})
}