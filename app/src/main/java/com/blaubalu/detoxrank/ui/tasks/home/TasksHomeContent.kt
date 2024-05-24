package com.blaubalu.detoxrank.ui.tasks.home

import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.data.Section
import com.blaubalu.detoxrank.data.local.LocalTasksDataProvider
import com.blaubalu.detoxrank.data.task.TaskDurationCategory
import com.blaubalu.detoxrank.service.TimerService
import com.blaubalu.detoxrank.ui.DetoxRankBottomNavigationBar
import com.blaubalu.detoxrank.ui.DetoxRankNavigationRail
import com.blaubalu.detoxrank.ui.DetoxRankTopAppBar
import com.blaubalu.detoxrank.ui.DetoxRankUiState
import com.blaubalu.detoxrank.ui.DetoxRankViewModel
import com.blaubalu.detoxrank.ui.DetoxRankViewModelProvider
import com.blaubalu.detoxrank.ui.NavigationDrawerContent
import com.blaubalu.detoxrank.ui.NavigationItemContent
import com.blaubalu.detoxrank.ui.rank.AchievementViewModel
import com.blaubalu.detoxrank.ui.tasks.task.TaskList
import com.blaubalu.detoxrank.ui.tasks.task.TaskViewModel
import com.blaubalu.detoxrank.ui.theme.Typography
import com.blaubalu.detoxrank.ui.utils.AnimationBox
import com.blaubalu.detoxrank.ui.utils.DetoxRankNavigationType
import kotlinx.coroutines.delay
import java.util.Calendar
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TasksHomeScreen(
    navigationItemContentList: List<NavigationItemContent>,
    timerService: TimerService,
    detoxRankUiState: DetoxRankUiState,
    detoxRankViewModel: DetoxRankViewModel,
    taskViewModel: TaskViewModel,
    achievementViewModel: AchievementViewModel,
    onTabPressed: ((Section) -> Unit),
    navigationType: DetoxRankNavigationType,
    modifier: Modifier = Modifier
) {
  if (navigationType == DetoxRankNavigationType.PERMANENT_NAVIGATION_DRAWER) {
    PermanentNavigationDrawer(drawerContent = {
      PermanentDrawerSheet(modifier.width(240.dp)) {
        NavigationDrawerContent(
            selectedDestination = detoxRankUiState.currentSection,
            onTabPressed = onTabPressed,
            navigationItemContentList = navigationItemContentList
        )
      }
    }
    ) {
      TasksContent(
          navigationItemContentList = navigationItemContentList,
          timerService = timerService,
          detoxRankUiState = detoxRankUiState,
          detoxRankViewModel = detoxRankViewModel,
          taskViewModel = taskViewModel,
          achievementViewModel = achievementViewModel,
          onTabPressed = onTabPressed,
          navigationType = navigationType
      )
    }
  } else {
    TasksContent(
        navigationItemContentList = navigationItemContentList,
        timerService = timerService,
        detoxRankUiState = detoxRankUiState,
        detoxRankViewModel = detoxRankViewModel,
        taskViewModel = taskViewModel,
        achievementViewModel = achievementViewModel,
        onTabPressed = onTabPressed,
        navigationType = navigationType
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun TasksContent(
    navigationItemContentList: List<NavigationItemContent>,
    timerService: TimerService,
    detoxRankUiState: DetoxRankUiState,
    onTabPressed: ((Section) -> Unit),
    navigationType: DetoxRankNavigationType,
    detoxRankViewModel: DetoxRankViewModel,
    achievementViewModel: AchievementViewModel,
    taskViewModel: TaskViewModel,
    modifier: Modifier = Modifier,
    viewModel: TasksHomeViewModel = viewModel(factory = DetoxRankViewModelProvider.Factory)
) {
  val tasksHomeUiState by viewModel.tasksHomeUiState.collectAsState()
  val userDataUiState by detoxRankViewModel.userDataUiState.collectAsState()
  val customTaskStartEndPadding = (LocalConfiguration.current.screenWidthDp / 6).dp
  val tasksToAdd = LocalTasksDataProvider.tasks
  val coroutineScope = rememberCoroutineScope()

  LaunchedEffect(Unit) {
    val availableRefreshes = detoxRankViewModel.getAvailableTaskRefreshes()
    detoxRankViewModel.setAvailableTaskRefreshes(availableRefreshes)
  }

  Row(modifier = modifier.fillMaxSize()) {
    // navigation rail (side)
    AnimatedVisibility(
        visible = navigationType == DetoxRankNavigationType.NAVIGATION_RAIL
    ) {
      DetoxRankNavigationRail(
          currentTab = detoxRankUiState.currentSection,
          onTabPressed = onTabPressed,
          navigationItemContentList = navigationItemContentList
      )
    }
    Scaffold(
        floatingActionButton = {
          FloatingActionButton(onClick = {
//                    coroutineScope.launch { // FILLDB uncomment to fill task db
////                        taskViewModel.deleteAllTasksInDb()
//                        tasksToAdd.forEach {
//                            taskViewModel.updateUiState(it.toTaskUiState())
//                            taskViewModel.insertTaskToDatabase()
//                        }
//                    }
            viewModel.invertCreateTaskMenuShownValue()
          }) {
            Icon(
                imageVector = Icons.Filled.AddTask,
                contentDescription = stringResource(id = R.string.add_task)
            )
          }
        },
        topBar = {
          Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
          ) {
            DetoxRankTopAppBar(detoxRankViewModel)
            Row {
              Icon(
                  imageVector = Icons.Filled.Refresh,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = modifier.padding(end = 5.dp)
              )
              AnimatedContent(
                  targetState = userDataUiState.availableTaskRefreshes,
                  transitionSpec = {
                    expandVertically() + fadeIn() togetherWith
                        slideOutVertically() + fadeOut()
                  },
                  label = ""
              ) { targetState ->
                Text(
                    "$targetState",
                    fontWeight = FontWeight.Bold,
                    modifier = modifier.padding(end = 30.dp)
                )
              }

            }
          }
        },
        bottomBar = {
          if (navigationType == DetoxRankNavigationType.BOTTOM_NAVIGATION)
            DetoxRankBottomNavigationBar(
                currentTab = detoxRankUiState.currentSection,
                onTabPressed = onTabPressed,
                navigationItemContentList = navigationItemContentList,
                modifier = Modifier.padding(bottom = 0.dp)
            )
        }
    ) { paddingValues ->
      // keep everything centered when on mobile screen size
      if (navigationType == DetoxRankNavigationType.BOTTOM_NAVIGATION) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxHeight()
                .zIndex(1f)
                // blocks simultaneous taps under the box
                .pointerInput(Unit) { detectTapGestures(onTap = {}) }
        ) {
          CreateTaskMenu(
              viewModel = viewModel,
              taskViewModel = taskViewModel,
              modifier = Modifier
                  .fillMaxHeight(0.75f)
                  .padding(start = 6.dp, end = 6.dp)
          )
        }
        AnimationBox {
          TaskList(
              timerService = timerService,
              taskList = tasksHomeUiState.taskList,
              detoxRankViewModel = detoxRankViewModel,
              achievementViewModel = achievementViewModel,
              modifier = Modifier
                  .padding(paddingValues)
                  .fillMaxWidth()
          )
        }
      } else {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxHeight()
                .zIndex(1f)
                // blocks simultaneous taps under the box
                .pointerInput(Unit) { detectTapGestures(onTap = {}) }
        ) {
          CreateTaskMenu(
              viewModel = viewModel,
              taskViewModel = taskViewModel,
              modifier = Modifier
                  .fillMaxHeight(0.85f)
                  .padding(
                      start = customTaskStartEndPadding,
                      end = customTaskStartEndPadding
                  )
          )
        }
        AnimationBox {
          TaskList(
              timerService = timerService,
              taskList = tasksHomeUiState.taskList,
              detoxRankViewModel = detoxRankViewModel,
              achievementViewModel = achievementViewModel,
              modifier = Modifier
                  .padding(paddingValues)
                  .fillMaxWidth()
          )
        }
      }

    }
  }
}

private fun initNextMonthCalendar(): Calendar {
  val nextMonth = Calendar.getInstance()
  nextMonth.set(Calendar.DAY_OF_MONTH, nextMonth.getActualMaximum(Calendar.DAY_OF_MONTH))
  nextMonth.set(Calendar.HOUR_OF_DAY, 23)
  nextMonth.set(Calendar.MINUTE, 59)
  nextMonth.set(Calendar.SECOND, 59)
  return nextMonth
}

private fun initMidnightCalendar(): Calendar {
  val midnight = Calendar.getInstance()
  midnight.set(Calendar.HOUR_OF_DAY, 23)
  midnight.set(Calendar.MINUTE, 59)
  midnight.set(Calendar.SECOND, 59)
  return midnight
}

/**
 * UI of a task label with a countdown timer
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TasksHeading(
    @StringRes headingRes: Int,
    timerService: TimerService,
    category: TaskDurationCategory,
    iconImageVector: ImageVector,
    modifier: Modifier = Modifier
) {
  var isLaunched by remember { mutableStateOf(false) }
  val nextMonth = initNextMonthCalendar()
  val midnight = initMidnightCalendar()
  val currentTimeMillis = System.currentTimeMillis()
  val timeDiff = midnight.timeInMillis - currentTimeMillis
  val secondsLeft = timeDiff / 1000
  val daysToNextMonth = (nextMonth.timeInMillis - currentTimeMillis) / 1000 / 60 / 60 / 24

  val timeLeftDays = daysToNextMonth.days
  val timeLeftHours = ((secondsLeft / 60 / 60) % 24).hours
  val timeLeftMinutes = ((secondsLeft / 60) % 60).minutes
  val timeLeftSeconds = ((secondsLeft) % 60).seconds

  if (!isLaunched) {
    timerService.disableTaskTimer()
    timerService.initTaskTimer()
    isLaunched = true
  }

  val taskDuration = timeLeftDays + timeLeftHours + timeLeftMinutes + timeLeftSeconds
  timerService.setTaskDuration(taskDuration)


  Row(
      modifier
          .padding(start = 25.dp, end = 25.dp, top = 25.dp, bottom = 10.dp)
          .fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
  ) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
          text = stringResource(headingRes),
          style = Typography.bodyLarge,
          modifier = Modifier.padding(end = 10.dp),
          fontSize = 22.sp
      )
      Icon(
          imageVector = iconImageVector,
          contentDescription = null,
          modifier = Modifier.size(25.dp)
      )
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      TaskTimer(category, timerService)
    }
  }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TaskTimer(
    category: TaskDurationCategory,
    timerService: TimerService
) {
  val dayOfWeekEu = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
  val daysRemainingWeek: Int = when (dayOfWeekEu) {
    Calendar.SUNDAY -> {
      0
    }

    else -> 7 - (dayOfWeekEu - 1)
  }
  var isVisible by remember { mutableStateOf(false) }
  val hoursRemaining by timerService.hoursDay
  val minutesRemaining by timerService.minutesDay
  val secondsRemaining by timerService.secondsDay

  val daysRemainingMonth by timerService.daysMonth

  LaunchedEffect(Unit) {
    delay(800)
    isVisible = true
  }

  AnimatedVisibility(
      visible = isVisible,
      enter = fadeIn() + slideInVertically(
          tween(
              700,
              easing = LinearOutSlowInEasing
          )
      ) { height -> height / 5 }
  ) {
    when (category) {
      TaskDurationCategory.Daily -> {
        Text(
            stringResource(
                id = R.string.tasklist_time_left,
                "${hoursRemaining}h ${minutesRemaining}min ${secondsRemaining}s"
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = Typography.bodyMedium
        )
      }

      TaskDurationCategory.Weekly -> {
        val formattedTimer = if (daysRemainingWeek >= 1) {
          "${daysRemainingWeek}d ${hoursRemaining}h"
        } else {
          "${hoursRemaining}h ${minutesRemaining}min ${secondsRemaining}s"
        }
        Text(
            stringResource(id = R.string.tasklist_time_left, formattedTimer),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = Typography.bodyMedium
        )
      }

      TaskDurationCategory.Monthly -> {
        val daysRemainingMonthInt = daysRemainingMonth.toInt()
        val formattedTimer = if (daysRemainingMonthInt >= 7) "${daysRemainingMonth}d"
        else if (daysRemainingMonthInt >= 1) "${daysRemainingMonth}d ${hoursRemaining}h"
        else "${hoursRemaining}h ${minutesRemaining}min ${secondsRemaining}s"

        Text(
            stringResource(id = R.string.tasklist_time_left, formattedTimer),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = Typography.bodyMedium
        )
      }

      TaskDurationCategory.Special -> {
        Icon(Icons.Filled.AllInclusive, contentDescription = null)
      }

      else -> {}
    }
  }
}
