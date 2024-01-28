package com.blaubalu.detoxrank.ui.theory.screens.chapter_hedonic_circuit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
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
import com.blaubalu.detoxrank.ui.theory.screens.ContinueIconButton

@Composable
fun CHHedonicCircuitPoint(
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
        CHHedonicCircuitPointBody()
        ContinueIconButton(
            onClick = onChapterContinue,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
fun CHHedonicCircuitPointBody() {
    val boldStyle = SpanStyle(
        color = if (isSystemInDarkTheme()) md_theme_dark_tertiary else md_theme_light_tertiary,
        fontWeight = FontWeight.Bold
    )

    Text(
        buildAnnotatedString {
            append(text = stringResource(id = R.string.chapter_hedonic_circuit_screen_3_pt_1))
            withStyle(style = boldStyle) { append(" once you were there") }
            append(".\n\n")
            append(text = stringResource(id = R.string.chapter_hedonic_circuit_screen_3_pt_2))
            withStyle(style = boldStyle) { append(" happier") }
            append(" or ")
            withStyle(style = boldStyle) { append("more content ") }
            append(text = stringResource(id = R.string.chapter_hedonic_circuit_screen_3_pt_3))
        },
        style = Typography.bodyLarge
    )
}

@Preview
@Composable
fun CHHedonicCircuitPointPreview() {
    CHHedonicCircuitPoint(onChapterContinue = { }) {
    }
}
