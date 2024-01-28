package com.blaubalu.detoxrank.ui.theory.screens.chapter_solution

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
import androidx.compose.ui.unit.dp
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.ui.theme.Typography
import com.blaubalu.detoxrank.ui.theory.TheoryImage
import com.blaubalu.detoxrank.ui.theory.screens.ContinueIconButton

@Composable
fun CHSolutionAdvice(
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
        CHSolutionAdviceBody()
        ContinueIconButton(
            onClick = onChapterContinue,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
fun CHSolutionAdviceBody(
    modifier: Modifier = Modifier
) {

    val darkTheme = isSystemInDarkTheme()

    Text(
        text = stringResource(id = R.string.chapter_solution_screen_2_pt_1),
        style = Typography.bodyLarge
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TheoryImage(
            imageRes = if (darkTheme)
                R.drawable.brain_tolerance
            else
                R.drawable.brain_tolerance_light
        )
    }

    Text(
        text = stringResource(id = R.string.chapter_solution_screen_2_pt_2),
        style = Typography.bodyLarge
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TheoryImage(
            imageRes = if (darkTheme)
                R.drawable.dopamine_social_media
            else
                R.drawable.dopamine_social_media_light
        )
    }

    Text(
        text = stringResource(id = R.string.chapter_solution_screen_2_pt_3),
        style = Typography.bodyLarge
    )


}
