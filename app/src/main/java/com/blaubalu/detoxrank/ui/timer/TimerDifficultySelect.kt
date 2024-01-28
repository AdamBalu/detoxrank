package com.blaubalu.detoxrank.ui.timer

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.data.TimerDifficulty
import com.blaubalu.detoxrank.data.TimerDifficultyCard
import com.blaubalu.detoxrank.service.TimerService
import com.blaubalu.detoxrank.service.TimerState
import com.blaubalu.detoxrank.ui.DetoxRankViewModel
import com.blaubalu.detoxrank.ui.theme.Typography
import com.blaubalu.detoxrank.ui.utils.Constants.RP_PERCENTAGE_GAIN_TIMER_EASY_DIFFICULTY
import com.blaubalu.detoxrank.ui.utils.Constants.RP_PERCENTAGE_GAIN_TIMER_HARD_DIFFICULTY
import com.blaubalu.detoxrank.ui.utils.Constants.RP_PERCENTAGE_GAIN_TIMER_MEDIUM_DIFFICULTY

@ExperimentalAnimationApi
@Composable
fun TimerDifficultySelectScreen(
    timerViewModel: TimerViewModel,
    timerService: TimerService,
    detoxRankViewModel: DetoxRankViewModel,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        timerViewModel.difficultySelectShown,
        enter = slideInVertically(animationSpec = tween(durationMillis = 500)) { height -> height } + fadeIn(
            animationSpec = tween(durationMillis = 500)
        ),
        exit = slideOutVertically(animationSpec = tween(durationMillis = 500)) { height -> height }
    ) {
        BackHandler {
            timerViewModel.setDifficultySelectShown(false)
        }
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(top = 0.dp, bottom = 0.dp, start = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        stringResource(R.string.difficulty_select_heading),
                        style = Typography.headlineMedium
                    )
                }
            }
            items(timerViewModel.difficultyList) { card ->
                DifficultyCard(
                    card = card,
                    timerViewModel = timerViewModel,
                    timerService = timerService,
                    detoxRankViewModel = detoxRankViewModel,
                    modifier = Modifier.padding(start = 20.dp, top = 5.dp, bottom = 5.dp, end = 20.dp)
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun DifficultyCard(
    card: TimerDifficultyCard,
    timerViewModel: TimerViewModel,
    timerService: TimerService,
    detoxRankViewModel: DetoxRankViewModel,
    modifier: Modifier = Modifier
) {
    val currentState by timerService.currentState

    val difficultyTitle = when(card.difficulty) {
        TimerDifficulty.Easy -> "Apprentice (+$RP_PERCENTAGE_GAIN_TIMER_EASY_DIFFICULTY% RP)"
        TimerDifficulty.Medium -> "Runner (+$RP_PERCENTAGE_GAIN_TIMER_MEDIUM_DIFFICULTY% RP)"
        TimerDifficulty.Hard -> "Master (+$RP_PERCENTAGE_GAIN_TIMER_HARD_DIFFICULTY% RP)"
    }

    val currentDifficulty = detoxRankViewModel.uiState.collectAsState().value.currentTimerDifficulty

    val cardEnabled = currentState != TimerState.Started

    val cardColor = if (currentDifficulty == card.difficulty) {
        when (card.difficulty) {
            TimerDifficulty.Easy -> MaterialTheme.colorScheme.tertiary
            TimerDifficulty.Medium -> MaterialTheme.colorScheme.primary
            TimerDifficulty.Hard -> MaterialTheme.colorScheme.error
        }
    } else {
        MaterialTheme.colorScheme.outline
    }

    Card(
        border = BorderStroke(4.dp, cardColor),
        modifier = modifier
            .fillMaxWidth()
            .height(270.dp)
            .clickable(enabled = cardEnabled) {
                timerViewModel.setDifficultySelectShown(false)
                detoxRankViewModel.setTimerDifficultyUiState(card.difficulty)
                detoxRankViewModel.setTimerDifficultyDatabase(card.difficulty)
            }
            .padding(top = 7.dp)
    ) {
        Box() {
            Image(
                painter = painterResource(id = card.backgroundImageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier
                .align(Alignment.TopStart)
                .padding(5.dp)
            ) {
                Text(
                    text = difficultyTitle,
                    style = Typography.headlineMedium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 15.dp, bottom = 5.dp, top = 5.dp),
                    color = cardColor
                )
                Card {
                    Column(
                        modifier = Modifier.padding(start = 20.dp, bottom = 10.dp, top = 10.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            if (card.difficulty != TimerDifficulty.Easy) "Avoid (scroll to see all):" else "Avoid:",
                            style = Typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 5.dp)
                        )
                        Column {
                            LazyColumn(modifier = Modifier.height(120.dp).width(190.dp)) {
                                items(card.avoidList) { avoidItem ->
                                    BannedItem(item = avoidItem)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
