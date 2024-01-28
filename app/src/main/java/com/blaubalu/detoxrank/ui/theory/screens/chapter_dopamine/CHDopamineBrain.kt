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
fun CHDopamineBrain(
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
        CHDopamineBrainBody()
        ContinueIconButton(
            onClick = onChapterContinue,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
fun CHDopamineBrainBody(
    modifier: Modifier = Modifier
) {

    val darkTheme = isSystemInDarkTheme()
    val boldStyle = SpanStyle(
        color = if (darkTheme) md_theme_dark_tertiary else md_theme_light_tertiary,
        fontWeight = FontWeight.Bold
    )

    Text(
        text = stringResource(id = R.string.chapter_dopamine_screen_2_pt_1),
        style = Typography.bodyLarge
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TheoryImage(
            imageRes = if (darkTheme)
                R.drawable.brain
            else
                R.drawable.brain_light,
            modifier = modifier.width(200.dp)
        )
    }

    Text(
        text = stringResource(id = R.string.chapter_dopamine_screen_2_pt_2),
        style = Typography.bodyLarge
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TheoryImage(
            imageRes = if (darkTheme)
                R.drawable.brain_neurons
            else
                R.drawable.brain_neurons_light,
            contentDescription = R.string.brain_neurons_content_description,
            imageLabel = R.string.brain_neurons_label,
            modifier = modifier.width(280.dp)
        )
    }

    Text(
        buildAnnotatedString {
            append(text = stringResource(id = R.string.chapter_dopamine_screen_2_pt_3))
            withStyle(style = boldStyle) { append(" neuron") }
            append(".")
        },
        style = Typography.bodyLarge
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TheoryImage(
            imageRes = if (darkTheme)
                R.drawable.neuron
            else
                R.drawable.neuron_light,
            contentDescription = R.string.neuron,
            imageLabel = R.string.neuron,
            modifier = modifier.width(250.dp)
        )
    }
}
