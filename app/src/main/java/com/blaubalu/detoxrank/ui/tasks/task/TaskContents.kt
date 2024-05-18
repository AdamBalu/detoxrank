package com.blaubalu.detoxrank.ui.tasks.task

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.data.task.Task
import com.blaubalu.detoxrank.data.task.TaskDurationCategory
import com.blaubalu.detoxrank.ui.DetoxRankViewModel
import com.blaubalu.detoxrank.ui.theme.Typography
import com.blaubalu.detoxrank.ui.utils.RankPointsGain
import com.blaubalu.detoxrank.ui.utils.getIcon
import com.blaubalu.detoxrank.ui.utils.toastShort
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TaskContents(
    task: Task,
    taskViewModel: TaskViewModel,
    detoxRankViewModel: DetoxRankViewModel,
    userTaskToBeDeleted: MutableState<Boolean>,
    taskToBeRefreshed: MutableState<Boolean>,
    rankPointsGain: Int,
    coroutineScope: CoroutineScope,
    context: Context,
    modifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.buildTaskContentModifier(task, userTaskToBeDeleted)
    ) {
        TaskIconAndDescription(
            task = task,
            rankPointsGain = rankPointsGain,
            userTaskToBeDeleted = userTaskToBeDeleted,
            modifier
        )
        TaskHandlingTrailingIcon(
            userTaskToBeDeleted = userTaskToBeDeleted,
            taskToBeRefreshed = taskToBeRefreshed,
            task = task,
            taskViewModel = taskViewModel,
            coroutineScope = coroutineScope,
            context = context,
            detoxRankViewModel = detoxRankViewModel,
            rankPointsGain = rankPointsGain,
            modifier
        )
    }
}

private fun Modifier.buildTaskContentModifier(
    task: Task,
    userTaskToBeDeleted: MutableState<Boolean>,
): Modifier = this
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
            15.dp
        } else {
            14.dp
        },
    )

@Composable
fun TaskText(
    visibleState: MutableTransitionState<Boolean>,
    fontStyle: FontStyle,
    modifier: Modifier,
    text: String
) {
    AnimatedVisibility(
        visibleState = visibleState
    ) {
        Text(
            text = text,
            style = Typography.bodyMedium,
            fontSize = 16.sp,
            fontStyle = fontStyle,
            modifier = modifier
        )
    }
}

@Composable
fun TaskTexts(
    task: Task,
    userTaskToBeDeleted: MutableState<Boolean>,
    modifier: Modifier
) {
    TaskText(
        visibleState = MutableTransitionState(!task.completed && !userTaskToBeDeleted.value),
        fontStyle = FontStyle.Normal,
        modifier = modifier.padding(bottom = 5.dp, start = 16.dp),
        text = task.description
    )

    TaskText(
        visibleState = MutableTransitionState(task.completed),
        fontStyle = FontStyle.Italic,
        modifier = modifier.padding(start = 38.dp),
        text = stringResource(R.string.task_completed)
    )

    TaskText(
        visibleState = MutableTransitionState(userTaskToBeDeleted.value),
        fontStyle = FontStyle.Italic,
        modifier = modifier.padding(start = 38.dp),
        text = stringResource(R.string.task_delete)
    )
}

@Composable
fun TaskHandlingTrailingIcon(
    userTaskToBeDeleted: MutableState<Boolean>,
    taskToBeRefreshed: MutableState<Boolean>,
    task: Task,
    taskViewModel: TaskViewModel,
    coroutineScope: CoroutineScope,
    context: Context,
    detoxRankViewModel: DetoxRankViewModel,
    rankPointsGain: Int,
    modifier: Modifier
) {
    if (userTaskToBeDeleted.value) {
        TaskIconDelete(
            task = task,
            taskViewModel = taskViewModel,
            context = context,
            coroutineScope = coroutineScope,
            modifier = modifier
        )
    } else if (taskToBeRefreshed.value) {
        TaskIconRefresh(
            task = task,
            taskViewModel = taskViewModel,
            detoxRankViewModel = detoxRankViewModel,
            coroutineScope = coroutineScope,
            taskToBeRefreshed = taskToBeRefreshed,
            context = context,
            modifier = modifier
        )
    } else {
        TaskCheckbox(
            task = task,
            taskViewModel = taskViewModel,
            coroutineScope = coroutineScope,
            detoxRankViewModel = detoxRankViewModel,
            rankPointsGain = rankPointsGain
        )
    }
}

@Composable
fun TaskIconAndDescription(
    task: Task,
    rankPointsGain: Int,
    userTaskToBeDeleted: MutableState<Boolean>,
    modifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(0.83f)
    ) {
        Column {
            Icon(
                imageVector = getIcon(task.iconCategory),
                contentDescription = null,
                modifier = modifier
                    .size(30.dp)
                    .padding(start = 0.dp, end = 5.dp)
                    .align(Alignment.CenterHorizontally)
            )
            RankPointsGain(
                rankPointsGain = rankPointsGain,
                plusIconSize = 10.dp,
                shieldIconSize = 11.dp,
                fontSize = 10.sp,
                horizontalArrangement = Arrangement.Center
            )
        }
        TaskTexts(task, userTaskToBeDeleted, modifier)
    }
}

@Composable
fun TaskIconRefresh(
    task: Task,
    taskViewModel: TaskViewModel,
    detoxRankViewModel: DetoxRankViewModel,
    coroutineScope: CoroutineScope,
    taskToBeRefreshed: MutableState<Boolean>,
    context: Context,
    modifier: Modifier
) {
    Icon(
        Icons.Default.Refresh,
        contentDescription = null,
        modifier = modifier
            .padding(end = 15.dp)
            .pointerInput(task) {
                detectTapGestures(
                    onTap = {
                        coroutineScope.launch {
                            val areRefreshesAvailable = detoxRankViewModel.decrementTaskRefreshes()
                            if (!areRefreshesAvailable) {
                                toastShort("No available task refreshes!", context)
                            } else {
                                taskViewModel.updateUiState(
                                    task
                                        .copy(
                                            completed = false,
                                            selectedAsCurrentTask = false,
                                            wasSelectedLastTime = true
                                        )
                                        .toTaskUiState()
                                )
                                taskViewModel.updateTask()
                                taskViewModel.refreshTask(task.durationCategory)
                                taskToBeRefreshed.value = false
                            }
                        }
                    })
            }
    )
}

@Composable
fun TaskIconDelete(
    task: Task,
    taskViewModel: TaskViewModel,
    context: Context,
    coroutineScope: CoroutineScope,
    modifier: Modifier
) {
    Icon(
        Icons.Default.Delete,
        contentDescription = null,
        modifier = modifier
            .padding(end = 15.dp)
            .pointerInput(task) {
                detectTapGestures(
                    onTap = {
                        coroutineScope.launch {
                            taskViewModel.deleteTask(task)
                            toastShort("Task deleted", context)
                        }
                    }
                )
            }
    )
}

@Composable
fun TaskCheckbox(
    task: Task,
    taskViewModel: TaskViewModel,
    coroutineScope: CoroutineScope,
    detoxRankViewModel: DetoxRankViewModel,
    rankPointsGain: Int
) {
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