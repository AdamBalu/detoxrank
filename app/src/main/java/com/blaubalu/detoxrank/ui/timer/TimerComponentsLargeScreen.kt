package com.blaubalu.detoxrank.ui.timer

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.service.TimerService
import com.blaubalu.detoxrank.ui.DetoxRankUiState
import com.blaubalu.detoxrank.ui.DetoxRankViewModel
import com.blaubalu.detoxrank.ui.theme.Typography
import com.blaubalu.detoxrank.ui.utils.calculateTimerFloatAddition
import com.blaubalu.detoxrank.ui.utils.calculateTimerRPGain
import com.blaubalu.detoxrank.ui.utils.getParamDependingOnScreenSizeDp
import com.blaubalu.detoxrank.ui.utils.getParamDependingOnScreenSizeDpLarge
import com.blaubalu.detoxrank.ui.utils.getParamDependingOnScreenSizeSpLarge
import com.hitanshudhawan.circularprogressbar.CircularProgressBar

@ExperimentalAnimationApi
@Composable
fun TimerClockLarge(
    timerService: TimerService
) {
    val progressSeconds by animateFloatAsState(
        targetValue = timerService.seconds.value.toFloat() * calculateTimerFloatAddition(50f, 60),
        label = ""
    )
    val progressMinutes by animateFloatAsState(
        targetValue = timerService.minutes.value.toFloat() * calculateTimerFloatAddition(39f, 60),
        label = ""
    )
    val progressHours by animateFloatAsState(
        targetValue = timerService.hours.value.toFloat() * calculateTimerFloatAddition(19.44f, 24),
        label = ""
    )

    val timerWidthIncrement =
        getParamDependingOnScreenSizeDpLarge((-20).dp, 0.dp, 0.dp, 10.dp, 30.dp, 60.dp)

    Box(contentAlignment = Alignment.Center) {
        Box {
            CircularProgressBar(
                modifier = Modifier
                    .width(328.dp + timerWidthIncrement)
                    .align(Alignment.Center),
                progress = progressSeconds,
                progressMax = 100f,
                progressBarColor =
                MaterialTheme.colorScheme.primary,
                progressBarWidth = 18.dp,
                backgroundProgressBarColor = Color.Transparent,
                backgroundProgressBarWidth = 1.dp,
                roundBorder = true,
                startAngle = 270f
            )
            CircularProgressBar(
                modifier = Modifier
                    .width(314.dp + timerWidthIncrement)
                    .align(Alignment.Center),
                progress = 50f,
                progressMax = 100f,
                progressBarColor =
                MaterialTheme.colorScheme.primary,
                progressBarWidth = 4.dp,
                backgroundProgressBarColor = Color.Transparent,
                backgroundProgressBarWidth = 1.dp,
                roundBorder = true,
                startAngle = 270f
            )
            CircularProgressBar(
                modifier = Modifier
                    .width(285.dp + timerWidthIncrement)
                    .align(Alignment.Center),
                progress = progressMinutes,
                progressMax = 100f,
                progressBarColor =
                MaterialTheme.colorScheme.secondary,
                progressBarWidth = 20.dp,
                backgroundProgressBarColor = Color.Transparent,
                backgroundProgressBarWidth = 1.dp,
                roundBorder = true,
                startAngle = 290f
            )
            CircularProgressBar(
                modifier = Modifier
                    .width(269.dp + timerWidthIncrement)
                    .align(Alignment.Center),
                progress = 39f,
                progressMax = 100f,
                progressBarColor = MaterialTheme.colorScheme.secondary,
                progressBarWidth = 4.dp,
                backgroundProgressBarColor = Color.Transparent,
                backgroundProgressBarWidth = 1.dp,
                roundBorder = true,
                startAngle = 290f
            )

            CircularProgressBar(
                modifier = Modifier
                    .width(240.dp + timerWidthIncrement)
                    .align(Alignment.Center),
                progress = progressHours,
                progressMax = 100f,
                progressBarColor =
                MaterialTheme.colorScheme.tertiary,
                progressBarWidth = 25.dp,
                backgroundProgressBarColor = Color.Transparent,
                backgroundProgressBarWidth = 1.dp,
                roundBorder = true,
                startAngle = 325f
            )

            CircularProgressBar(
                modifier = Modifier
                    .width(220.dp + timerWidthIncrement)
                    .align(Alignment.Center),
                progress = 19.44f,
                progressMax = 100f,
                progressBarColor =
                MaterialTheme.colorScheme.tertiary,
                progressBarWidth = 4.dp,
                backgroundProgressBarColor = Color.Transparent,
                backgroundProgressBarWidth = 1.dp,
                roundBorder = true,
                startAngle = 325f
            )
        }
        TimerTimeInNumbers(timerService = timerService)
    }
}

/**
 * Consists of a timer difficulty select button, timer RP gain and day streak UIs (for large screens)
 */
@ExperimentalAnimationApi
@Composable
fun TimerFooterLarge(
    timerService: TimerService,
    detoxRankUiState: DetoxRankUiState,
    detoxRankViewModel: DetoxRankViewModel,
    timerViewModel: TimerViewModel,
    modifier: Modifier = Modifier
) {
    val days by timerService.days

    val currentScreenHeight = LocalConfiguration.current.screenHeightDp

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.End,
        modifier = modifier
            .fillMaxHeight()
            .padding(start = 35.dp, end = 35.dp, bottom = 35.dp, top = 35.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "DAY STREAK",
                style = Typography.bodySmall
            )
            Text(
                days,
                style = Typography.headlineLarge,
                textAlign = TextAlign.Center,
                fontSize = getParamDependingOnScreenSizeSpLarge(
                    p1 = 21.sp,
                    p2 = 25.sp,
                    p3 = 30.sp,
                    p4 = 40.sp,
                    otherwise = 40.sp
                ),
                modifier = Modifier.padding(top = 12.dp)
            )
        }
        Row {
            CollectAccumulatedRpButton(detoxRankViewModel, timerService, modifier)
            AccumulatedRp(
                detoxRankViewModel = detoxRankViewModel,
                currentScreenHeight = currentScreenHeight,
                timerService = timerService,
                modifier = modifier
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.difficulty),
                style = Typography.bodySmall
            )
            DifficultySelect(
                onClick = { timerViewModel.setDifficultySelectShown(true) },
                timerService = timerService,
                detoxRankUiState = detoxRankUiState,
                detoxRankViewModel = detoxRankViewModel
            )
        }
    }
}
