package com.blaubalu.detoxrank.ui.theory.screens.chapter_reinforcement

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.ui.theme.Typography
import com.blaubalu.detoxrank.ui.theme.md_theme_dark_tertiary
import com.blaubalu.detoxrank.ui.theme.md_theme_light_tertiary
import com.blaubalu.detoxrank.ui.theory.TheoryImage
import com.blaubalu.detoxrank.ui.theory.screens.ContinueIconButton

@Composable
fun CHReinforcementRewardCircuit(
    onChapterContinue: () -> Unit,
    modifier: Modifier = Modifier,
    backHandler: () -> Unit
) {
    val scrollState = rememberScrollState()
    BackHandler(onBack = backHandler)

    Column(
        modifier = modifier
            .padding(start = 26.dp, end = 26.dp, top = 26.dp)
            .verticalScroll(state = scrollState)
    ) {
        CHReinforcementRewardCircuitBody()
        ContinueIconButton(
            onClick = onChapterContinue,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
fun CHReinforcementRewardCircuitBody(
    modifier: Modifier = Modifier
) {
    val darkTheme = isSystemInDarkTheme()
    val boldStyle = SpanStyle(
        color = if (darkTheme) md_theme_dark_tertiary else md_theme_light_tertiary,
        fontWeight = FontWeight.Bold
    )

    Text(
        buildAnnotatedString {
            append(text = stringResource(id = R.string.chapter_reinforcement_screen_2_pt_1))
            withStyle(style = boldStyle) { append(" reward circuit") }
            append(".")
        },
        style = Typography.bodyLarge
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TheoryImage(
            imageRes = if (darkTheme)
                R.drawable.reward_circuit
            else
                R.drawable.reward_circuit_light,
            imageLabel = R.string.reward_circuit_label,
            contentDescription = R.string.reward_circuit_label,
            modifier = Modifier.padding(top = 30.dp, bottom = 30.dp)
        )
    }

    Text(
        buildAnnotatedString {
            withStyle(style = boldStyle) { append("Behavior reinforcement ") }
            append(text = stringResource(id = R.string.chapter_reinforcement_screen_2_pt_2))
        },
        style = Typography.bodyLarge
    )
}

@Preview
@Composable
fun CHReinforcementRewardCircuitPreview() {
    CHReinforcementRewardCircuit(onChapterContinue = { }) {

    }
}
