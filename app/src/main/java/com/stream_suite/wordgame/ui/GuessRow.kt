package com.stream_suite.wordgame.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.stream_suite.wordgame.LetterState
import com.stream_suite.wordgame.R
import com.stream_suite.wordgame.data.WORD_LENGTH
import com.stream_suite.wordgame.ui.theme.AlmostYellow
import com.stream_suite.wordgame.ui.theme.CorrectGreen
import com.stream_suite.wordgame.ui.theme.InactiveGray
import com.stream_suite.wordgame.ui.theme.OffWhite
import com.stream_suite.wordgame.ui.theme.Typography

@Composable
fun GuessRow(letters: List<Letter>, gameIndex: Int) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Row(
        modifier = Modifier.padding(mediumPadding),
        horizontalArrangement = Arrangement.spacedBy(mediumPadding)
    ) {
        letters.forEach { letter: Letter ->
            val cardColor = when (letter.states[gameIndex]) {
                LetterState.Initial -> OffWhite
                LetterState.Correct -> CorrectGreen
                LetterState.Exists -> AlmostYellow
                LetterState.Missing -> InactiveGray
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                colors = CardDefaults.cardColors(containerColor = cardColor)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = letter.letter.toString(),
                        style = Typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BlankGuessRowPreview(letters: List<Letter> = List(WORD_LENGTH) { Letter(' ') }) {
    GuessRow(letters, 0)
}

@Preview(showBackground = true)
@Composable
fun InProgressGuessRowPreview(
    letters: List<Letter> = listOf(
        Letter('S', mutableListOf(LetterState.Initial)),
        Letter('A', mutableListOf(LetterState.Initial)),
        Letter(' '),
        Letter(' '),
        Letter(' ')
    )
) {
    GuessRow(letters, 0)
}

@Preview(showBackground = true)
@Composable
fun SubmittedGuessRowPreview(
    letters: List<Letter> = listOf(
        Letter('S', mutableListOf(LetterState.Missing)),
        Letter('A', mutableListOf(LetterState.Exists)),
        Letter('L', mutableListOf(LetterState.Missing)),
        Letter('E', mutableListOf(LetterState.Correct)),
        Letter('T', mutableListOf(LetterState.Exists))
    )
) {
    GuessRow(letters, 0)
}