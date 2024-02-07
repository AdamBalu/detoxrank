package com.blaubalu.detoxrank.ui.timer

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.service.ServiceHelper
import com.blaubalu.detoxrank.service.TimerService
import com.blaubalu.detoxrank.service.TimerState
import com.blaubalu.detoxrank.ui.DetoxRankUiState
import com.blaubalu.detoxrank.ui.DetoxRankViewModel
import com.blaubalu.detoxrank.ui.rank.AchievementViewModel
import com.blaubalu.detoxrank.ui.theme.Typography
import com.blaubalu.detoxrank.ui.utils.Constants
import com.blaubalu.detoxrank.ui.utils.calculateTimerFloatAddition
import com.blaubalu.detoxrank.ui.utils.calculateTimerRPGain
import com.blaubalu.detoxrank.ui.utils.getParamDependingOnScreenSizeDpLarge
import com.blaubalu.detoxrank.ui.utils.getParamDependingOnScreenSizeSpLarge
import com.hitanshudhawan.circularprogressbar.CircularProgressBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        getParamDependingOnScreenSizeDpLarge(-20.dp, 0.dp, 0.dp, 10.dp, 30.dp, 60.dp)

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
        TimerTimeInNumbersLarge(timerService = timerService)
    }
}

@ExperimentalAnimationApi
@Composable
fun TimerTimeUnitDigitAnimatedPairLarge(timeUnit: String, color: Color, label: String = "") {
    AnimatedContent(
        targetState = timeUnit,
        transitionSpec = {
            addAnimation().using(SizeTransform(clip = false))
        }, label = label
    ) {
        Text(
            text = it,
            style = TextStyle(
                fontSize = 55.sp,
                fontWeight = FontWeight.Bold,
                color = color,
            ),
            modifier = Modifier.padding(end = 15.dp)
        )
    }
}

@ExperimentalAnimationApi
@Composable
fun TimerTimeInNumbersLarge(
    timerService: TimerService
) {
    val hours by timerService.hours
    val minutes by timerService.minutes
    val seconds by timerService.seconds

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        timerService.updateTimerTimeLaunchedEffect(context)
    }
    Row {
        TimerTimeUnitDigitAnimatedPairLarge(hours, MaterialTheme.colorScheme.tertiary)
        TimerTimeUnitDigitAnimatedPairLarge(minutes, MaterialTheme.colorScheme.secondary)
        TimerTimeUnitDigitAnimatedPairLarge(seconds, MaterialTheme.colorScheme.primary)
    }
}

@ExperimentalAnimationApi
@Composable
fun TimerStartStopButtonLarge(
    timerService: TimerService,
    detoxRankViewModel: DetoxRankViewModel,
    achievementViewModel: AchievementViewModel,
    modifier: Modifier = Modifier
) {
    val currentState by timerService.currentState
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var wasButtonClicked by remember { mutableStateOf(false) }
    val timerRPGain = calculateTimerRPGain(timerService)
//// prepared implementation of a dismiss button when clicking on a finish timer button
//// decision was made that this wouldn't be included yet, as the users did not complain about
//// the current functionality
//    var wasDismissAlertClicked by remember { mutableStateOf(false) }
//    var showEndDetoxDialog by remember { mutableStateOf(false) }

//    if (showEndDetoxDialog) {
//        SaveTimerProgressDialog(
//            onConfirm = {
//                ServiceHelper.triggerForegroundService(
//                    context = context,
//                    action = Constants.ACTION_SERVICE_CANCEL
//                )
//                coroutineScope.launch {
//                    detoxRankViewModel.updateTimerStarted(false)
//                }
//                showEndDetoxDialog = false
//            },
//            onDismiss = {
//                if (!wasDismissAlertClicked) {
//                    wasDismissAlertClicked = true
//                    Toast.makeText(context, "Tap again if you really wish to delete", Toast.LENGTH_SHORT).show()
//                    coroutineScope.launch {
//                        delay(3000)
//                        wasDismissAlertClicked = false
//                    }
//                } else {
//                    wasDismissAlertClicked = false
//                    ServiceHelper.triggerForegroundService(
//                        context = context,
//                        action = Constants.ACTION_SERVICE_CANCEL
//                    )
//                    coroutineScope.launch {
//                        detoxRankViewModel.updateTimerStarted(false)
//                    }
//                    showEndDetoxDialog = false
//                }
//            }
//        )
//    }

    fun stopTimerService() {
        if (!ServiceHelper.triggerForegroundService(
                context = context,
                action = Constants.ACTION_SERVICE_CANCEL
            )
        ) {
            Toast.makeText(
                context,
                "You need to allow the permission to start the timer",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            coroutineScope.launch {
                achievementViewModel.achieveTimerAchievements(timerService.days.value.toInt())
                detoxRankViewModel.updateTimerStarted(false)
                detoxRankViewModel.updateUserRankPoints(timerRPGain)
            }
            wasButtonClicked = false
        }
    }

    fun handleTimerStopButtonPress() {
        if (!wasButtonClicked) {
            Toast
                .makeText(context, "Double tap to end the timer", Toast.LENGTH_SHORT)
                .show()
            wasButtonClicked = true
            coroutineScope.launch {
                delay(2000)
                wasButtonClicked = false
            }
        } else {
            stopTimerService()
        }
    }

    fun startTimerService() {
        if (!ServiceHelper.triggerForegroundService(
                context = context,
                action = Constants.ACTION_SERVICE_START
            )
        ) {
            Toast.makeText(
                context,
                "You need to allow the permission to start the timer",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            coroutineScope.launch {
                achievementViewModel.achieveAchievement(Constants.ID_START_TIMER)
                detoxRankViewModel.updateTimerStartedTimeMillis()
                detoxRankViewModel.updateTimerStarted(true)
            }
        }
    }

    Box(modifier = modifier.padding(top = 60.dp)) {
        if (currentState == TimerState.Started) {
            OutlinedIconButton(
                onClick = { handleTimerStopButtonPress() },
                modifier = modifier.fillMaxWidth(0.5f).align(Alignment.BottomCenter).padding(top = 0.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Stop,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 5.dp)
                    )
                    Text(
                        text = "Finish",
                        style = Typography.bodySmall,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            FilledIconButton(
                onClick = { startTimerService() },
                modifier = Modifier.fillMaxWidth(0.5f).align(Alignment.BottomCenter)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 5.dp)
                    )
                    Text(
                        text = "Start Detox",
                        style = Typography.bodySmall,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontSize = 14.sp
                    )
                }
            }
        }
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
    val currentScreenWidth = LocalConfiguration.current.screenWidthDp

    val points = calculateTimerRPGain(timerService)
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
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
                )
            )
        }
        Column(
            modifier = modifier.padding(bottom = 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.timer_accumulated_points_heading),
                style = Typography.bodySmall,
                fontSize = getParamDependingOnScreenSizeSpLarge(
                    p1 = 11.sp,
                    p2 = 12.sp,
                    p3 = 12.sp,
                    p4 = 12.sp,
                    otherwise = 11.sp
                )
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "$points",
                    modifier = Modifier.padding(top = 0.dp, end = 10.dp),
                    style = Typography.headlineLarge,
                    letterSpacing = 1.sp,
                    fontSize = if (points > 999) {
                        15.sp
                    } else {
                        getParamDependingOnScreenSizeSpLarge(
                            p1 = 21.sp,
                            p2 = 25.sp,
                            p3 = 30.sp,
                            p4 = 40.sp,
                            otherwise = 40.sp
                        )
                    }
                )
                Image(
                    painterResource(id = R.drawable.rank_points_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(
                            if (points > 999 || (currentScreenWidth < 800 && currentScreenHeight < 400)) {
                                25.dp
                            } else {
                                30.dp
                            }
                        )
                        .padding(top = 5.dp)
                )

            }
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
