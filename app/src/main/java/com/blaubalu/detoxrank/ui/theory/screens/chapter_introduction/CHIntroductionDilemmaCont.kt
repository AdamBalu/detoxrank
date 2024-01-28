package com.blaubalu.detoxrank.ui.theory.screens.chapter_introduction

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
import androidx.compose.ui.unit.dp
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.ui.DetoxRankViewModel
import com.blaubalu.detoxrank.ui.theme.Typography
import com.blaubalu.detoxrank.ui.theme.md_theme_dark_tertiary
import com.blaubalu.detoxrank.ui.theme.md_theme_light_tertiary
import com.blaubalu.detoxrank.ui.theory.TheoryViewModel
import com.blaubalu.detoxrank.ui.theory.screens.CompleteChapterIconButton

@Composable
fun CHIntroDilemmaCont(
    onChapterDone: () -> Unit,
    modifier: Modifier = Modifier,
    backHandler: () -> Unit,
    chapterName: String,
    theoryViewModel: TheoryViewModel,
    detoxRankViewModel: DetoxRankViewModel
) {
    val scrollState = rememberScrollState()
    BackHandler(onBack = backHandler)

    Column(
        modifier = modifier
            .padding(start = 26.dp, end = 26.dp, top = 26.dp)
            .verticalScroll(state = scrollState)
    ) {
        CHIntroDilemmaContBody()
        CompleteChapterIconButton(
            onClick = onChapterDone,
            modifier = Modifier.align(Alignment.End),
            chapterName = chapterName,
            theoryViewModel = theoryViewModel,
            detoxRankViewModel = detoxRankViewModel
        )
    }
}

@Composable
fun CHIntroDilemmaContBody() {
    Text(
        buildAnnotatedString {
            append(text = stringResource(id = R.string.chapter_intro_screen_3_pt_1))
            withStyle(
                style = SpanStyle(
                    color = if (isSystemInDarkTheme()) md_theme_dark_tertiary else md_theme_light_tertiary,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(" struggle ")
            }
            append(text = "to really")
            withStyle(
                style = SpanStyle(
                    color = if (isSystemInDarkTheme()) md_theme_dark_tertiary else md_theme_light_tertiary,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(" understand ")
            }
            append(stringResource(id = R.string.chapter_intro_screen_3_pt_2))
        },
        style = Typography.bodyLarge
    )
}
