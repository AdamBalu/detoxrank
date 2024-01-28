package com.blaubalu.detoxrank.ui.tasks.task

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.data.TimerDifficulty
import com.blaubalu.detoxrank.data.task.Task
import com.blaubalu.detoxrank.data.task.TaskDurationCategory
import com.blaubalu.detoxrank.data.task.TaskIconCategory
import com.blaubalu.detoxrank.service.TimerService
import com.blaubalu.detoxrank.ui.DetoxRankViewModel
import com.blaubalu.detoxrank.ui.DetoxRankViewModelProvider
import com.blaubalu.detoxrank.ui.rank.AchievementViewModel
import com.blaubalu.detoxrank.ui.tasks.home.TasksHeading
import com.blaubalu.detoxrank.ui.theme.Typography
import com.blaubalu.detoxrank.ui.theme.epic_purple
import com.blaubalu.detoxrank.ui.theme.epic_purple_toned_down
import com.blaubalu.detoxrank.ui.theme.md_theme_light_tertiaryContainerVariant
import com.blaubalu.detoxrank.ui.theme.rank_color_ultra_dark
import com.blaubalu.detoxrank.ui.theme.rank_color_ultra_light
import com.blaubalu.detoxrank.ui.theme.*
import com.blaubalu.detoxrank.ui.utils.AnimationBox
import com.blaubalu.detoxrank.ui.utils.Constants.DAILY_TASK_RP_GAIN
import com.blaubalu.detoxrank.ui.utils.Constants.MONTHLY_TASK_RP_GAIN
import com.blaubalu.detoxrank.ui.utils.Constants.RP_PERCENTAGE_GAIN_TIMER_EASY_DIFFICULTY
import com.blaubalu.detoxrank.ui.utils.Constants.RP_PERCENTAGE_GAIN_TIMER_HARD_DIFFICULTY
import com.blaubalu.detoxrank.ui.utils.Constants.RP_PERCENTAGE_GAIN_TIMER_MEDIUM_DIFFICULTY
import com.blaubalu.detoxrank.ui.utils.Constants.SPECIAL_TASK_RP_GAIN
import com.blaubalu.detoxrank.ui.utils.Constants.UNCATEGORIZED_TASK_RP_GAIN
import com.blaubalu.detoxrank.ui.utils.Constants.WEEKLY_TASK_RP_GAIN
import com.blaubalu.detoxrank.ui.utils.RankPointsGain
import com.blaubalu.detoxrank.ui.utils.getIcon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
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
        item {
            if (!taskList.none { it.durationCategory == TaskDurationCategory.Uncategorized })
                TasksHeading(
                    headingRes = R.string.tasklist_heading_custom,
                    timerService = timerService,
                    category = TaskDurationCategory.Uncategorized,
                    iconImageVector = Icons.Filled.Face
                )
        }
        items(taskList.filter { it.durationCategory == TaskDurationCategory.Uncategorized }) { task ->
            AnimationBox {
                Task(
                    task = task,
                    detoxRankViewModel = detoxRankViewModel,
                    achievementViewModel = achievementViewModel,
                    taskViewModel = taskViewModel
                )
            }
        }
        item {
            TasksHeading(
                headingRes = R.string.tasklist_heading_daily,
                timerService = timerService,
                category = TaskDurationCategory.Daily,
                iconImageVector = Icons.Filled.Today
            )
        }
        items(taskList.filter { it.durationCategory == TaskDurationCategory.Daily }) { task ->
            AnimationBox {
                Task(
                    task = task,
                    detoxRankViewModel = detoxRankViewModel,
                    achievementViewModel = achievementViewModel,
                    taskViewModel = taskViewModel
                )
            }
        }
        item {
            TasksHeading(
                headingRes = R.string.tasklist_heading_weekly,
                timerService = timerService,
                category = TaskDurationCategory.Weekly,
                iconImageVector = Icons.Filled.DateRange
            )
        }
        items(taskList.filter { it.durationCategory == TaskDurationCategory.Weekly }) { task ->
            AnimationBox {
                Task(
                    task = task,
                    detoxRankViewModel = detoxRankViewModel,
                    achievementViewModel = achievementViewModel,
                    taskViewModel = taskViewModel
                )
            }
        }
        item {
            TasksHeading(
                headingRes = R.string.tasklist_heading_monthly,
                timerService = timerService,
                category = TaskDurationCategory.Monthly,
                iconImageVector = Icons.Filled.CalendarMonth
            )
        }
        items(taskList.filter { it.durationCategory == TaskDurationCategory.Monthly }) { task ->
            AnimationBox {
                Task(
                    task = task,
                    detoxRankViewModel = detoxRankViewModel,
                    achievementViewModel = achievementViewModel,
                    taskViewModel = taskViewModel
                )
            }
        }
        item {
            if (!taskList.none { it.durationCategory == TaskDurationCategory.Special })
                TasksHeading(
                    headingRes = R.string.tasklist_heading_special,
                    timerService = timerService,
                    category = TaskDurationCategory.Special,
                    iconImageVector = Icons.Filled.ElectricBolt
                )
        }
        items(taskList.filter { it.durationCategory == TaskDurationCategory.Special }) { task ->
            AnimationBox {
                Task(
                    task = task,
                    detoxRankViewModel = detoxRankViewModel,
                    achievementViewModel = achievementViewModel,
                    taskViewModel = taskViewModel
                )
            }
        }
        item {
            Spacer(modifier = Modifier.padding(bottom = 75.dp))
        }
    }
}

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

    LaunchedEffect(Unit) {
        val timerDifficultyFromDb = detoxRankViewModel.getUserTimerDifficulty()
        detoxRankViewModel.setCurrentTimerDifficulty(timerDifficultyFromDb)
        val isTimerStartedFromDb = detoxRankViewModel.getUserTimerStarted()
        detoxRankViewModel.setTimerStarted(isTimerStartedFromDb)
    }

    val uiState = detoxRankViewModel.uiState.collectAsState().value
    val multiplier = if (uiState.isTimerStarted) {
        when (uiState.currentTimerDifficulty) {
            TimerDifficulty.Easy -> RP_PERCENTAGE_GAIN_TIMER_EASY_DIFFICULTY / 100.0
            TimerDifficulty.Medium -> RP_PERCENTAGE_GAIN_TIMER_MEDIUM_DIFFICULTY / 100.0
            TimerDifficulty.Hard -> RP_PERCENTAGE_GAIN_TIMER_HARD_DIFFICULTY / 100.0
        }
    } else {
        0.0
    }

    val rankPointsGain = when (task.durationCategory) {
        TaskDurationCategory.Daily -> DAILY_TASK_RP_GAIN + (DAILY_TASK_RP_GAIN * multiplier).toInt()
        TaskDurationCategory.Weekly -> WEEKLY_TASK_RP_GAIN + (WEEKLY_TASK_RP_GAIN * multiplier).toInt()
        TaskDurationCategory.Monthly -> MONTHLY_TASK_RP_GAIN + (MONTHLY_TASK_RP_GAIN * multiplier).toInt()
        TaskDurationCategory.Uncategorized -> UNCATEGORIZED_TASK_RP_GAIN + (UNCATEGORIZED_TASK_RP_GAIN * multiplier).toInt()
        TaskDurationCategory.Special -> SPECIAL_TASK_RP_GAIN + (SPECIAL_TASK_RP_GAIN * multiplier).toInt()
    }

    val darkTheme = isSystemInDarkTheme()

    val userTaskToBeDeleted = remember { mutableStateOf(false) }
    val taskToBeRefreshed = remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 16.dp)
            .height(if (task.completed || userTaskToBeDeleted.value) IntrinsicSize.Min else IntrinsicSize.Max)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
            .pointerInput(task) {
                if (task.durationCategory == TaskDurationCategory.Uncategorized ||
                    task.durationCategory == TaskDurationCategory.Special
                ) {
                    detectTapGestures(
                        onTap = {
                            if (userTaskToBeDeleted.value) {
                                userTaskToBeDeleted.value = false
                            } else {
                                Toast
                                    .makeText(
                                        context,
                                        "Double tap to complete!",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        },
                        onLongPress = {
                            userTaskToBeDeleted.value = !userTaskToBeDeleted.value
                        },
                        onDoubleTap = {
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
                    )
                } else {
                    detectTapGestures(
                        onTap = {
                            if (!taskToBeRefreshed.value) {
                                taskViewModel.updateUiState(
                                    task
                                        .copy(completed = !task.completed)
                                        .toTaskUiState()
                                )
                                coroutineScope.launch {
                                    taskViewModel.updateTask()
                                }
                            } else {
                                taskToBeRefreshed.value = !taskToBeRefreshed.value
                            }
                        },
                        onLongPress = {
                            if (!task.completed)
                                taskToBeRefreshed.value = !taskToBeRefreshed.value
                        }
                    )
                }
            },
        colors = if (task.completed) {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.tertiary
            )
        } else {
            if (taskToBeRefreshed.value) {
                CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
            } else {
                when (task.durationCategory) {
                    TaskDurationCategory.Daily ->
                        if (darkTheme)
                            CardDefaults.cardColors(MaterialTheme.colorScheme.onSecondary)
                        else
                            CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)

                    TaskDurationCategory.Weekly ->
                        if (darkTheme)
                            CardDefaults.cardColors(MaterialTheme.colorScheme.onTertiary)
                        else
                            CardDefaults.cardColors(md_theme_light_tertiaryContainerVariant)

                    TaskDurationCategory.Monthly ->
                        if (darkTheme)
                            CardDefaults.cardColors(MaterialTheme.colorScheme.onError)
                        else
                            CardDefaults.cardColors(MaterialTheme.colorScheme.errorContainer)

                    TaskDurationCategory.Uncategorized ->
                        if (userTaskToBeDeleted.value) {
                            CardDefaults.cardColors(MaterialTheme.colorScheme.errorContainer)
                        } else {
                            if (darkTheme)
                                CardDefaults.cardColors(rank_color_ultra_dark)
                            else
                                CardDefaults.cardColors(rank_color_ultra_light)
                        }

                    TaskDurationCategory.Special ->
                        if (darkTheme)
                            CardDefaults.cardColors(epic_purple)
                        else
                            CardDefaults.cardColors(epic_purple_toned_down)
                }
            }
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 15.dp,
                    end = 10.dp,
                    top = if (task.completed) {
                        2.dp
                    } else if (userTaskToBeDeleted.value) {
                        15.dp
                    } else {
                        18.dp
                    },
                    bottom = if (task.completed) {
                        2.dp
                    } else if (userTaskToBeDeleted.value) {
                        0.dp
                    } else {
                        14.dp
                    },
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(0.83f)
            ) {
                Column {
                    Icon(
                        imageVector = getIcon(task.iconCategory),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(start = 0.dp, end = 5.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    AnimatedVisibility(
                        visibleState = MutableTransitionState(!task.completed && !userTaskToBeDeleted.value),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        RankPointsGain(
                            rankPointsGain = rankPointsGain,
                            plusIconSize = 10.dp,
                            shieldIconSize = 11.dp,
                            fontSize = 10.sp,
                            horizontalArrangement = Arrangement.Center
                        )
                    }
                }

                AnimatedVisibility(
                    visibleState = MutableTransitionState(!task.completed && !userTaskToBeDeleted.value),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column(
                        modifier = Modifier.padding(start = 15.dp)
                    ) {
                        Text(
                            text = task.description,
                            style = Typography.bodyMedium,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 5.dp)
                        )
                    }
                }

                AnimatedVisibility(
                    visibleState = MutableTransitionState(task.completed),
                    enter = expandHorizontally() + fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = stringResource(R.string.task_completed),
                        style = Typography.bodyMedium,
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.padding(start = 38.dp)
                    )
                }

                AnimatedVisibility(
                    visibleState = MutableTransitionState(userTaskToBeDeleted.value),
                    enter = expandHorizontally() + fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        stringResource(R.string.task_delete),
                        style = Typography.bodyMedium,
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.padding(start = 38.dp)
                    )
                }
            }

            if (userTaskToBeDeleted.value) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 15.dp)
                        .pointerInput(task) {
                            detectTapGestures(
                                onTap = {
                                    coroutineScope.launch {
                                        taskViewModel.deleteTask(task)
                                        Toast
                                            .makeText(context, "Task deleted", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                            )
                        }
                )
            } else if (taskToBeRefreshed.value) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 15.dp)
                        .pointerInput(task) {
                            detectTapGestures(
                                onTap = {
                                    taskViewModel.updateUiState(
                                        task
                                            .copy(
                                                completed = false,
                                                selectedAsCurrentTask = false,
                                                wasSelectedLastTime = true
                                            )
                                            .toTaskUiState()
                                    )
                                    coroutineScope.launch {
                                        taskViewModel.updateTask()
                                        taskViewModel.refreshTask(task.durationCategory)
                                    }
                                    taskToBeRefreshed.value = false
                                })
                        }
                )
            } else {
                Checkbox(
                    checked = task.completed,
                    onCheckedChange = {
                        taskViewModel.updateUiState(
                            task
                                .copy(completed = !task.completed)
                                .toTaskUiState()
                        )
                        coroutineScope.launch {
                            taskViewModel.updateTask()
                        }
                        if (task.durationCategory == TaskDurationCategory.Uncategorized || task.durationCategory == TaskDurationCategory.Special) {
                            coroutineScope.launch {
                                if (task.durationCategory == TaskDurationCategory.Uncategorized) {
                                    taskViewModel.deleteTask(task)
                                } else { // special tasks get updated completion parameter
                                    taskViewModel.updateUiState(
                                        task.copy(
                                            completed = true,
                                            selectedAsCurrentTask = false
                                        ).toTaskUiState()
                                    )
                                    delay(600)
                                    taskViewModel.updateTask()
                                }
                                detoxRankViewModel.updateUserRankPoints(rankPointsGain)
                            }
                        }
                    }
                )
            }
        }
    }
}
