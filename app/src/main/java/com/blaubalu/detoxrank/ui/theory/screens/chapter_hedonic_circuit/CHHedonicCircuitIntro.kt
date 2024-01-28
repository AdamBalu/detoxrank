package com.blaubalu.detoxrank.ui.theory.screens.chapter_hedonic_circuit

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.ui.theme.Typography
import com.blaubalu.detoxrank.ui.theory.TheoryImage
import com.blaubalu.detoxrank.ui.theory.screens.ContinueIconButton

@Composable
fun CHHedonicCircuitIntro(
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
        CHHedonicCircuitIntroBody()
        ContinueIconButton(
            onClick = onChapterContinue,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
fun CHHedonicCircuitIntroBody(
    modifier: Modifier = Modifier
) {
    val darkTheme = isSystemInDarkTheme()
    Text(
        text = stringResource(R.string.chapter_hedonic_circuit_screen_1_pt_1),
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
            contentDescription = R.string.reward_circuit_label,
            imageLabel = R.string.reward_circuit_label
        )
    }

    Text(
        text = stringResource(R.string.chapter_hedonic_circuit_screen_1_pt_2),
        style = Typography.bodyLarge
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TheoryImage(
            imageRes = if (darkTheme)
                R.drawable.hedonic_circuit
            else
                R.drawable.hedonic_circuit_light,
            contentDescription = R.string.hedonic_circuit_label,
            imageLabel = R.string.hedonic_circuit_label
        )
    }

    Text(
        text = stringResource(R.string.chapter_hedonic_circuit_screen_1_pt_3),
        style = Typography.bodyLarge
    )
}

@Preview
@Composable
fun CHHedonicCircuitIntroPreview() {
    CHHedonicCircuitIntro(onChapterContinue = { }) {

    }
}
