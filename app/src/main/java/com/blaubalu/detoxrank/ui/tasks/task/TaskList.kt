package com.blaubalu.detoxrank.ui.tasks.task

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.data.TimerDifficulty
import com.blaubalu.detoxrank.data.task.Task
import com.blaubalu.detoxrank.data.task.TaskDurationCategory
import com.blaubalu.detoxrank.service.TimerService
import com.blaubalu.detoxrank.ui.DetoxRankUiState
import com.blaubalu.detoxrank.ui.DetoxRankViewModel
import com.blaubalu.detoxrank.ui.DetoxRankViewModelProvider
import com.blaubalu.detoxrank.ui.rank.AchievementViewModel
import com.blaubalu.detoxrank.ui.tasks.home.TasksHeading
import com.blaubalu.detoxrank.ui.utils.AnimationBox
import com.blaubalu.detoxrank.ui.utils.Constants.DAILY_TASK_RP_GAIN
import com.blaubalu.detoxrank.ui.utils.Constants.MONTHLY_TASK_RP_GAIN
import com.blaubalu.detoxrank.ui.utils.Constants.RP_PERCENTAGE_GAIN_TIMER_EASY_DIFFICULTY
import com.blaubalu.detoxrank.ui.utils.Constants.RP_PERCENTAGE_GAIN_TIMER_HARD_DIFFICULTY
import com.blaubalu.detoxrank.ui.utils.Constants.RP_PERCENTAGE_GAIN_TIMER_MEDIUM_DIFFICULTY
import com.blaubalu.detoxrank.ui.utils.Constants.SPECIAL_TASK_RP_GAIN
import com.blaubalu.detoxrank.ui.utils.Constants.UNCATEGORIZED_TASK_RP_GAIN
import com.blaubalu.detoxrank.ui.utils.Constants.WEEKLY_TASK_RP_GAIN
import com.blaubalu.detoxrank.ui.utils.toastShort
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@Composable
fun TaskList(
  timerService: TimerService,
  taskList: List<Task>,
  detoxRankViewModel: DetoxRankViewModel,
  achievementViewModel: AchievementViewModel,
  modifier: Modifier = Modifier,
  taskViewModel: TaskViewModel = viewModel(factory = DetoxRankViewModelProvider.Factory)
) {
  LazyColumn(
    modifier = modifier
      .fillMaxWidth()
  ) {
    renderTasksByCategory(
      taskList = taskList,
      timerService = timerService,
      detoxRankViewModel = detoxRankViewModel,
      achievementViewModel = achievementViewModel,
      taskViewModel = taskViewModel
    )
    item {
      Spacer(modifier = Modifier.padding(bottom = 75.dp))
    }
  }
}


@ExperimentalAnimationApi
private fun LazyListScope.renderTasksByCategory(
  taskList: List<Task>,
  timerService: TimerService,
  detoxRankViewModel: DetoxRankViewModel,
  achievementViewModel: AchievementViewModel,
  taskViewModel: TaskViewModel
) {
  TaskDurationCategory.entries.forEach { category ->
    val filteredTasks = taskList.filter { it.durationCategory == category }
    if (filteredTasks.isNotEmpty()) {
      getHeading(timerService, category)
      getItems(
        filteredTaskList = filteredTasks,
        detoxRankViewModel = detoxRankViewModel,
        achievementViewModel = achievementViewModel,
        taskViewModel = taskViewModel
      )
    }
  }
}

@ExperimentalAnimationApi
private fun LazyListScope.getHeading(timerService: TimerService, category: TaskDurationCategory) {
  val headingRes = when (category) {
    TaskDurationCategory.Uncategorized -> R.string.tasklist_heading_custom
    TaskDurationCategory.Daily -> R.string.tasklist_heading_daily
    TaskDurationCategory.Weekly -> R.string.tasklist_heading_weekly
    TaskDurationCategory.Monthly -> R.string.tasklist_heading_monthly
    TaskDurationCategory.Special -> R.string.tasklist_heading_special
  }

  return item {
    TasksHeading(
      headingRes = headingRes,
      timerService = timerService,
      category = category,
      iconImageVector = getCategoryIcon(category)
    )
  }
}

private fun getCategoryIcon(category: TaskDurationCategory): ImageVector {
  return when (category) {
    TaskDurationCategory.Uncategorized -> Icons.Filled.Face
    TaskDurationCategory.Daily -> Icons.Filled.Today
    TaskDurationCategory.Weekly -> Icons.Filled.DateRange
    TaskDurationCategory.Monthly -> Icons.Filled.CalendarMonth
    TaskDurationCategory.Special -> Icons.Filled.ElectricBolt
  }
}

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalAnimationApi
private fun LazyListScope.getItems(
  filteredTaskList: List<Task>,
  detoxRankViewModel: DetoxRankViewModel,
  achievementViewModel: AchievementViewModel,
  taskViewModel: TaskViewModel,
) {
  return items(filteredTaskList, key = { task -> task.description }) { task ->
    Task(
      task = task,
      detoxRankViewModel = detoxRankViewModel,
      achievementViewModel = achievementViewModel,
      taskViewModel = taskViewModel,
      modifier = Modifier.animateItemPlacement()
    )
  }
}

private fun getTaskMultiplier(uiState: DetoxRankUiState): Double = if (uiState.isTimerStarted) {
  when (uiState.currentTimerDifficulty) {
    TimerDifficulty.Easy -> RP_PERCENTAGE_GAIN_TIMER_EASY_DIFFICULTY
    TimerDifficulty.Medium -> RP_PERCENTAGE_GAIN_TIMER_MEDIUM_DIFFICULTY
    TimerDifficulty.Hard -> RP_PERCENTAGE_GAIN_TIMER_HARD_DIFFICULTY
  } / 100.0
} else {
  0.0
}

private fun getRPGain(task: Task, multiplier: Double): Int {
  val rpGainMap = mapOf(
    TaskDurationCategory.Daily to DAILY_TASK_RP_GAIN,
    TaskDurationCategory.Weekly to WEEKLY_TASK_RP_GAIN,
    TaskDurationCategory.Monthly to MONTHLY_TASK_RP_GAIN,
    TaskDurationCategory.Uncategorized to UNCATEGORIZED_TASK_RP_GAIN,
    TaskDurationCategory.Special to SPECIAL_TASK_RP_GAIN
  )

  return rpGainMap[task.durationCategory]?.let {
    (it + it * multiplier).toInt()
  } ?: 0
}

private suspend fun initializeUserTimerState(detoxRankViewModel: DetoxRankViewModel) {
  val timerDifficultyFromDb = detoxRankViewModel.getUserTimerDifficulty()
  detoxRankViewModel.setCurrentTimerDifficulty(timerDifficultyFromDb)
  val isTimerStartedFromDb = detoxRankViewModel.getUserTimerStarted()
  detoxRankViewModel.setTimerStarted(isTimerStartedFromDb)
}

private fun Modifier.buildTaskModifier(
  task: Task,
  coroutineScope: CoroutineScope,
  taskViewModel: TaskViewModel,
  achievementViewModel: AchievementViewModel,
  detoxRankViewModel: DetoxRankViewModel,
  rankPointsGain: Int,
  taskToBeEdited: MutableState<Boolean>,
  context: Context
): Modifier {
  return this
    .padding(vertical = 4.dp, horizontal = 16.dp)
    .animateContentSize(
      animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMediumLow
      )
    )
    .height(
      if (task.completed || taskToBeDeleted(
          task,
          taskToBeEdited
        )
      ) IntrinsicSize.Min else IntrinsicSize.Max
    )
    .pointerInput(task) {
      if (task.durationCategory == TaskDurationCategory.Uncategorized ||
        task.durationCategory == TaskDurationCategory.Special
      ) {
        detectTapGestures(
          onTap = { handleSpecialCategoryTap(task, taskToBeEdited, context) },
          onLongPress = { taskToBeEdited.value = !taskToBeEdited.value },
          onDoubleTap = {
            handleSpecialCategoryDoubleTap(
              task,
              coroutineScope,
              taskViewModel,
              achievementViewModel,
              detoxRankViewModel,
              rankPointsGain
            )
          }
        )
      } else {
        detectTapGestures(
          onTap = {
            handleRegularCategoryTap(
              task,
              taskToBeEdited,
              taskViewModel,
              coroutineScope
            )
          },
          onLongPress = { handleRegularCategoryLongPress(task, taskToBeEdited) }
        )
      }
    }
}

private fun handleSpecialCategoryTap(
  task: Task,
  taskToBeEdited: MutableState<Boolean>,
  context: Context,
) {
  if (taskToBeDeleted(task, taskToBeEdited)) {
    taskToBeEdited.value = false
  } else {
    toastShort("Double tap to complete!", context)
  }
}

private fun handleSpecialCategoryDoubleTap(
  task: Task,
  coroutineScope: CoroutineScope,
  taskViewModel: TaskViewModel,
  achievementViewModel: AchievementViewModel,
  detoxRankViewModel: DetoxRankViewModel,
  rankPointsGain: Int
) {
  coroutineScope.launch {
    if (task.durationCategory == TaskDurationCategory.Special) {
      taskViewModel.updateUiState(
        task
          .copy(completed = true, selectedAsCurrentTask = false)
          .toTaskUiState()
      )
      achievementViewModel.achieveAchievement(task.specialTaskID)
      taskViewModel.updateTask()
    } else {
      taskViewModel.updateUiState(
        task
          .copy(completed = true)
          .toTaskUiState()
      )
      taskViewModel.updateTask()
      taskViewModel.deleteTask(task)
    }
    detoxRankViewModel.updateUserRankPoints(rankPointsGain)
  }
}

private fun handleRegularCategoryTap(
  task: Task,
  taskToBeEdited: MutableState<Boolean>,
  taskViewModel: TaskViewModel,
  coroutineScope: CoroutineScope
) {
  if (taskToBeEdited.value) {
    taskToBeEdited.value = false
    return
  }

  taskViewModel.updateUiState(
    task
      .copy(completed = !task.completed)
      .toTaskUiState()
  )

  coroutineScope.launch {
    taskViewModel.updateTask()
  }
}

private fun handleRegularCategoryLongPress(
  task: Task,
  taskToBeEdited: MutableState<Boolean>
) {
  if (!task.completed)
    taskToBeEdited.value = !taskToBeEdited.value
}

fun isUsualTask(task: Task) = task.durationCategory in listOf(
  TaskDurationCategory.Daily,
  TaskDurationCategory.Weekly,
  TaskDurationCategory.Monthly
)

fun taskToBeDeleted(task: Task, taskToBeEdited: MutableState<Boolean>) =
  taskToBeEdited.value && task.durationCategory == TaskDurationCategory.Uncategorized

fun taskToBeRefreshed(task: Task, taskToBeEdited: MutableState<Boolean>) =
  taskToBeEdited.value && isUsualTask(task)

@Composable
fun Task(
  task: Task,
  taskViewModel: TaskViewModel,
  detoxRankViewModel: DetoxRankViewModel,
  achievementViewModel: AchievementViewModel,
  modifier: Modifier = Modifier
) {
  val coroutineScope = rememberCoroutineScope()
  val context = LocalContext.current
  val isVisible = remember { mutableStateOf(true) }

  LaunchedEffect(Unit) {
    initializeUserTimerState(detoxRankViewModel)
  }

  val uiState = detoxRankViewModel.uiState.collectAsState().value
  val multiplier = remember { getTaskMultiplier(uiState) }
  val rankPointsGain = remember { getRPGain(task, multiplier) }
  val darkTheme = isSystemInDarkTheme()
  val taskToBeEdited = remember { mutableStateOf(false) }
  val taskColors = remember { TaskColors() }


  val currentModifier = modifier.buildTaskModifier(
    task = task,
    taskToBeEdited = taskToBeEdited,
    coroutineScope = coroutineScope,
    taskViewModel = taskViewModel,
    achievementViewModel = achievementViewModel,
    detoxRankViewModel = detoxRankViewModel,
    rankPointsGain = rankPointsGain,
    context = context
  )

  AnimatedVisibility(
    visible = isVisible.value,
    exit = shrinkOut(),
    enter = expandIn()
  ) {
    Card(
      modifier = currentModifier,
      colors = taskColors(
        task = task,
        taskColors = taskColors,
        taskToBeDeleted = taskToBeDeleted(task, taskToBeEdited),
        taskToBeRefreshed = taskToBeRefreshed(task, taskToBeEdited),
        darkTheme = darkTheme
      )
    ) {
      AnimationBox {
        TaskContents(
          task = task,
          taskViewModel = taskViewModel,
          detoxRankViewModel = detoxRankViewModel,
          taskToBeEdited = taskToBeEdited,
          rankPointsGain = rankPointsGain,
          coroutineScope = coroutineScope,
          isVisible = isVisible,
          context = context,
          modifier = modifier
        )
      }
    }
  }
}

@Composable
fun taskColors(
  task: Task,
  taskColors: TaskColors,
  taskToBeDeleted: Boolean,
  taskToBeRefreshed: Boolean,
  darkTheme: Boolean
): CardColors {
  if (task.completed) {
    return taskColors.completedTaskColors()
  }

  if (taskToBeRefreshed) {
    return taskColors.defaultTaskColors()
  }

  return when (task.durationCategory) {
    TaskDurationCategory.Daily -> taskColors.dailyTaskColors(darkTheme)
    TaskDurationCategory.Weekly -> taskColors.weeklyTaskColors(darkTheme)
    TaskDurationCategory.Monthly -> taskColors.monthlyTaskColors(darkTheme)
    TaskDurationCategory.Uncategorized -> taskColors.uncategorizedTaskColors(
      taskToBeDeleted,
      darkTheme
    )

    TaskDurationCategory.Special -> taskColors.specialTaskColors(darkTheme)
  }
}


