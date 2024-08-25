package com.blaubalu.detoxrank.ui.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.data.TimerDifficulty
import com.blaubalu.detoxrank.service.TimerService
import com.blaubalu.detoxrank.ui.DetoxRankViewModel
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_100_TASKS
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_10_TASKS
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_250_TASKS
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_25_TASKS
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_50_TASKS
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_5_TASKS
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_ALL_CH
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_CH_1
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_CH_2
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_CH_3
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_CH_4
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_CH_5
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_CH_6
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_FIRST_TASK
import com.blaubalu.detoxrank.ui.utils.Constants.ID_READ_100_PAGES
import com.blaubalu.detoxrank.ui.utils.Constants.ID_READ_10_BOOKS
import com.blaubalu.detoxrank.ui.utils.Constants.ID_READ_20_PAGES
import com.blaubalu.detoxrank.ui.utils.Constants.ID_READ_250_PAGES
import com.blaubalu.detoxrank.ui.utils.Constants.ID_READ_50_PAGES
import com.blaubalu.detoxrank.ui.utils.Constants.ID_READ_5_BOOKS
import com.blaubalu.detoxrank.ui.utils.Constants.ID_READ_A_BOOK
import com.blaubalu.detoxrank.ui.utils.Constants.ID_RUN_10_KM
import com.blaubalu.detoxrank.ui.utils.Constants.ID_RUN_3_KM
import com.blaubalu.detoxrank.ui.utils.Constants.ID_RUN_5_KM
import com.blaubalu.detoxrank.ui.utils.Constants.ID_RUN_7_KM
import com.blaubalu.detoxrank.ui.utils.Constants.ID_START_TIMER
import com.blaubalu.detoxrank.ui.utils.Constants.ID_TIMER_14_DAYS
import com.blaubalu.detoxrank.ui.utils.Constants.ID_TIMER_30_DAYS
import com.blaubalu.detoxrank.ui.utils.Constants.ID_TIMER_3_DAYS
import com.blaubalu.detoxrank.ui.utils.Constants.ID_TIMER_7_DAYS
import com.blaubalu.detoxrank.ui.utils.Constants.TIMER_HARD_DIFFICULTY_MULTIPLIER
import com.blaubalu.detoxrank.ui.utils.Constants.TIMER_MEDIUM_DIFFICULTY_MULTIPLIER
import com.blaubalu.detoxrank.ui.utils.Constants.TIMER_RP_GAIN_PER_SECOND

/**
 * Formats time for the timer notification
 */
fun formatTime(days: String, seconds: String, minutes: String, hours: String): String {
  return "Day $days, $hours:$minutes:$seconds"
}

fun toastShort(text: String, context: Context) {
  Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun toastLong(text: String, context: Context) {
  Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}

/**
 * Returns padded integer with 2 digit length
 */
fun Int.pad(): String {
  return this.toString().padStart(2, '0')
}

@ExperimentalAnimationApi
fun calculateTimerRPGain(
  detoxRankViewModel: DetoxRankViewModel,
  timerService: TimerService,
): Double {
  val timerStartTimeMillis = detoxRankViewModel.userDataUiState.value.timerStartTimeMillis
  val lastRpGatherTimeMillis = detoxRankViewModel.userDataUiState.value.lastTimerRpGatherTime
  val timeToCutOutFromSeconds = (lastRpGatherTimeMillis / 1000) - (timerStartTimeMillis / 1000)

  val timerTimePassed: Int =
    timerService.seconds.value.toInt() +
        timerService.minutes.value.toInt() * 60 +
        timerService.hours.value.toInt() * 60 * 60 +
        timerService.days.value.toInt() * 60 * 60 * 24

  val timerDifficulty = detoxRankViewModel.uiState.value.currentTimerDifficulty
  val timerDiffMultipliers = mapOf(
    TimerDifficulty.Hard to TIMER_HARD_DIFFICULTY_MULTIPLIER,
    TimerDifficulty.Medium to TIMER_MEDIUM_DIFFICULTY_MULTIPLIER
  )

  return (timerTimePassed - timeToCutOutFromSeconds) * TIMER_RP_GAIN_PER_SECOND * (timerDiffMultipliers[timerDifficulty]
    ?: 1.0)
}

/**
 * Returns drawable ID representation of a given level
 * @param level integer representation of a level
 * @return drawable ID of a provided level
 */
@DrawableRes
fun getLevelDrawableId(level: Int): Int {
  return when (level) {
    1 -> R.drawable.level_1
    2 -> R.drawable.level_2
    3 -> R.drawable.level_3
    4 -> R.drawable.level_4
    5 -> R.drawable.level_5
    6 -> R.drawable.level_6
    7 -> R.drawable.level_7
    8 -> R.drawable.level_8
    9 -> R.drawable.level_9
    10 -> R.drawable.level_10
    11 -> R.drawable.level_11
    12 -> R.drawable.level_12
    13 -> R.drawable.level_13
    14 -> R.drawable.level_14
    15 -> R.drawable.level_15
    16 -> R.drawable.level_16
    17 -> R.drawable.level_17
    18 -> R.drawable.level_18
    19 -> R.drawable.level_19
    20 -> R.drawable.level_20
    21 -> R.drawable.level_21
    22 -> R.drawable.level_22
    23 -> R.drawable.level_23
    24 -> R.drawable.level_24
    25 -> R.drawable.level_25
    else -> R.drawable.level_1
  }
}

// level 25 is awarded for 39 236 accumulated xp
/**
 * Returns number of a user level given by xp count
 * @param xpPoints amount of user XP
 * @return integer representation of a level given by XP count
 */
fun getCurrentLevelFromXP(xpPoints: Int): Int {
  var value = 100.0
  var accumulated = value.toInt()
  var result = 1
  while (accumulated < xpPoints) {
    accumulated += (value + value * 0.2).toInt()
    value += value * 0.2
    result++
  }
  if (result > 25)
    return 25
  return result
}

fun getCurrentProgressBarProgression(xpPoints: Int): Float {
  var value = 100.0
  var accumulated = value.toInt()
  var lowerBound = accumulated
  var result = 1
  while (accumulated < xpPoints) {
    accumulated += (value + value * 0.2).toInt()
    value += value * 0.2
    result++
    if (accumulated < xpPoints) {
      lowerBound = accumulated
    }
  }
  if (result > 25) {
    return 1f
  }
  return (xpPoints.toFloat() - lowerBound) / (accumulated - lowerBound)
}

/**
 * Returns a given parameter in Dp depending on screen size. The bigger the screen size, the
 * latest parameter is returned. User for small screen sizes.
 */
@Composable
fun getParamDependingOnScreenSizeDp(p1: Dp?, p2: Dp?, p3: Dp?, p4: Dp?, otherwise: Dp): Dp {
  val currentScreenHeight = LocalConfiguration.current.screenHeightDp
  val currentScreenWidth = LocalConfiguration.current.screenWidthDp
  return if (currentScreenHeight < 600 && currentScreenWidth < 340) p1 ?: 0.dp
  else if (currentScreenHeight < 700 && currentScreenWidth < 370) p2 ?: 0.dp
  else if (currentScreenHeight < 800 && currentScreenWidth < 400) p3 ?: 0.dp
  else if (currentScreenHeight < 900 && currentScreenWidth < 500) p4 ?: 0.dp
  else otherwise
}

/**
 * Returns a given parameter in Dp depending on screen size. The bigger the screen size, the
 * latest parameter is returned. User for large screen sizes.
 */
@Composable
fun getParamDependingOnScreenSizeDpLarge(
  p1: Dp?,
  p2: Dp?,
  p3: Dp?,
  p4: Dp?,
  p5: Dp?,
  otherwise: Dp
): Dp {
  val currentScreenHeight = LocalConfiguration.current.screenHeightDp
  val currentScreenWidth = LocalConfiguration.current.screenWidthDp
  return if (currentScreenHeight < 340 && currentScreenWidth < 600) p1 ?: 0.dp
  else if (currentScreenHeight < 370 && currentScreenWidth < 700) p2 ?: 0.dp
  else if (currentScreenHeight < 400 && currentScreenWidth < 800) p3 ?: 0.dp
  else if (currentScreenHeight < 500 && currentScreenWidth < 900) p4 ?: 0.dp
  else if (currentScreenHeight < 600 && currentScreenWidth < 1200) p5 ?: 0.dp
  else otherwise
}

/**
 * Returns a given parameter in Sp depending on screen size. The bigger the screen size, the
 * latest parameter is returned. User for small screen sizes.
 */
@Composable
fun getParamDependingOnScreenSizeSp(
  p1: TextUnit?,
  p2: TextUnit?,
  p3: TextUnit?,
  p4: TextUnit?,
  otherwise: TextUnit
): TextUnit {
  val configuration = LocalConfiguration.current
  val (screenHeight, screenWidth) = remember {
    configuration.screenHeightDp to configuration.screenWidthDp
  }

  return when {
    screenHeight < 600 && screenWidth < 340 -> p1 ?: 0.sp
    screenHeight < 700 && screenWidth < 370 -> p2 ?: 0.sp
    screenHeight < 800 && screenWidth < 400 -> p3 ?: 0.sp
    screenHeight < 900 && screenWidth < 500 -> p4 ?: 0.sp
    else -> otherwise
  }
}

/**
 * Returns a given parameter in Sp depending on screen size. The bigger the screen size, the
 * latest parameter is returned. User for large screen sizes.
 */
@Composable
fun getParamDependingOnScreenSizeSpLarge(
  p1: TextUnit?,
  p2: TextUnit?,
  p3: TextUnit?,
  p4: TextUnit?,
  otherwise: TextUnit
): TextUnit {
  val currentScreenHeight = LocalConfiguration.current.screenHeightDp
  val currentScreenWidth = LocalConfiguration.current.screenWidthDp
  return if (currentScreenHeight < 340 && currentScreenWidth < 600) p1 ?: 0.sp
  else if (currentScreenHeight < 370 && currentScreenWidth < 700) p2 ?: 0.sp
  else if (currentScreenHeight < 400 && currentScreenWidth < 800) p3 ?: 0.sp
  else if (currentScreenHeight < 500 && currentScreenWidth < 900) p4 ?: 0.sp
  else otherwise
}

fun calculateTimerFloatAddition(progressLength: Float, numberOfUnitsInBiggerUnit: Int): Float =
  progressLength / (numberOfUnitsInBiggerUnit - 1)

/**
 * Gets achievement drawable depending on it's ID in the database
 * @param isDarkTheme defines whether the system is in dark theme
 * @return achievement Drawable ID depending on it's ID in the database
 */
@DrawableRes
fun getAchievementDrawableFromId(id: Int, isDarkTheme: Boolean): Int {
  return if (isDarkTheme) {
    when (id) {
      ID_RUN_3_KM -> R.drawable.run3km
      ID_RUN_5_KM -> R.drawable.run5km
      ID_RUN_7_KM -> R.drawable.run7km
      ID_RUN_10_KM -> R.drawable.run10km
      ID_READ_20_PAGES -> R.drawable.read20pages
      ID_READ_50_PAGES -> R.drawable.read50pages
      ID_READ_100_PAGES -> R.drawable.read100pages
      ID_READ_250_PAGES -> R.drawable.read250pages
      ID_READ_A_BOOK -> R.drawable.read1book
      ID_READ_5_BOOKS -> R.drawable.read5books
      ID_READ_10_BOOKS -> R.drawable.read10books
      ID_FINISH_CH_1 -> R.drawable.finishchapterintroduction
      ID_FINISH_CH_2 -> R.drawable.finishchapterdopamine
      ID_FINISH_CH_3 -> R.drawable.finishchapterreinforcement
      ID_FINISH_CH_4 -> R.drawable.finishchaptertolerance
      ID_FINISH_CH_5 -> R.drawable.finishchapterhedoniccircuit
      ID_FINISH_CH_6 -> R.drawable.finishchaptersolution
      ID_FINISH_ALL_CH -> R.drawable.finishallchapters
      ID_FINISH_FIRST_TASK -> R.drawable.finishfirsttask
      ID_FINISH_5_TASKS -> R.drawable.finish5tasks
      ID_FINISH_10_TASKS -> R.drawable.finish10tasks
      ID_FINISH_25_TASKS -> R.drawable.finish25tasks
      ID_FINISH_50_TASKS -> R.drawable.finish50tasks
      ID_FINISH_100_TASKS -> R.drawable.finish100tasks
      ID_FINISH_250_TASKS -> R.drawable.finish250tasks
      ID_START_TIMER -> R.drawable.startthetimer
      ID_TIMER_3_DAYS -> R.drawable.timer3days
      ID_TIMER_7_DAYS -> R.drawable.timer7days
      ID_TIMER_14_DAYS -> R.drawable.timer14days
      ID_TIMER_30_DAYS -> R.drawable.timer30days
      else -> R.drawable.gold1
    }
  } else {
    when (id) {
      ID_RUN_3_KM -> R.drawable.run3kmlight
      ID_RUN_5_KM -> R.drawable.run5kmlight
      ID_RUN_7_KM -> R.drawable.run7kmlight
      ID_RUN_10_KM -> R.drawable.run10kmlight
      ID_READ_20_PAGES -> R.drawable.read20pageslight
      ID_READ_50_PAGES -> R.drawable.read50pageslight
      ID_READ_100_PAGES -> R.drawable.read100pageslight
      ID_READ_250_PAGES -> R.drawable.read250pageslight
      ID_READ_A_BOOK -> R.drawable.read1book
      ID_READ_5_BOOKS -> R.drawable.read5books
      ID_READ_10_BOOKS -> R.drawable.read10books
      ID_FINISH_CH_1 -> R.drawable.finishchapterintroduction
      ID_FINISH_CH_2 -> R.drawable.finishchapterdopaminelight
      ID_FINISH_CH_3 -> R.drawable.finishchapterreinforcement
      ID_FINISH_CH_4 -> R.drawable.finishchaptertolerancelight
      ID_FINISH_CH_5 -> R.drawable.finishchapterhedoniccircuit
      ID_FINISH_CH_6 -> R.drawable.finishchaptersolution
      ID_FINISH_ALL_CH -> R.drawable.finishallchapters
      ID_FINISH_FIRST_TASK -> R.drawable.finishfirsttask
      ID_FINISH_5_TASKS -> R.drawable.finish5tasks
      ID_FINISH_10_TASKS -> R.drawable.finish10tasks
      ID_FINISH_25_TASKS -> R.drawable.finish25tasks
      ID_FINISH_50_TASKS -> R.drawable.finish50tasks
      ID_FINISH_100_TASKS -> R.drawable.finish100tasks
      ID_FINISH_250_TASKS -> R.drawable.finish250tasks
      ID_START_TIMER -> R.drawable.startthetimer
      ID_TIMER_3_DAYS -> R.drawable.timer3days
      ID_TIMER_7_DAYS -> R.drawable.timer7days
      ID_TIMER_14_DAYS -> R.drawable.timer14days
      ID_TIMER_30_DAYS -> R.drawable.timer30days
      else -> R.drawable.gold1
    }
  }
}
