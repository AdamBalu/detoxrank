package com.blaubalu.detoxrank.ui.theory.screens.chapter_dopamine

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.ui.theme.Typography
import com.blaubalu.detoxrank.ui.theme.md_theme_dark_tertiary
import com.blaubalu.detoxrank.ui.theme.md_theme_light_tertiary
import com.blaubalu.detoxrank.ui.theory.TheoryImage
import com.blaubalu.detoxrank.ui.theory.screens.ContinueIconButton

@Composable
fun CHDopaminePoint(
    modifier: Modifier = Modifier,
    onChapterContinue: () -> Unit,
    backHandler: () -> Unit
) {
    val scrollState = rememberScrollState()
    BackHandler(onBack = backHandler)

    Column(
        modifier = modifier
            .padding(start = 26.dp, end = 26.dp)
            .verticalScroll(state = scrollState)
    ) {
        CHDopaminePointBody()
        ContinueIconButton(
            onClick = onChapterContinue,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
fun CHDopaminePointBody() {
    val boldStyle = SpanStyle(
        color = if (isSystemInDarkTheme()) md_theme_dark_tertiary else md_theme_light_tertiary,
        fontWeight = FontWeight.Bold
    )

    Text(
        buildAnnotatedString {
            append(text = stringResource(id = R.string.chapter_dopamine_screen_4_pt_1))
            withStyle(style = boldStyle) { append(" reinforces your behaviors ") }
            append(text = stringResource(id = R.string.chapter_dopamine_screen_4_pt_2))
        },
        style = Typography.bodyLarge
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TheoryImage(
            imageRes = R.drawable.want_like_difference,
            modifier = Modifier.width(250.dp)
        )
    }

    Text(
        buildAnnotatedString {
            append(text = stringResource(id = R.string.chapter_dopamine_screen_4_pt_3))
            withStyle(style = boldStyle) { append(" wanting ") }
            append(text = stringResource(id = R.string.chapter_dopamine_screen_4_pt_4))
            withStyle(style = boldStyle) { append(" liking ") }
            append(text = stringResource(id = R.string.chapter_dopamine_screen_4_pt_5))
        },
        style = Typography.bodyLarge
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TheoryImage(
            imageRes = R.drawable.time_passing,
            modifier = Modifier.width(250.dp)
        )
    }
    Text(
        text = stringResource(id = R.string.chapter_dopamine_screen_4_pt_6),
        style = Typography.bodyLarge
    )
}