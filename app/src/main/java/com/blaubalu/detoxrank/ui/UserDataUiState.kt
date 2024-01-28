package com.blaubalu.detoxrank.ui

import com.blaubalu.detoxrank.data.TimerDifficulty
import com.blaubalu.detoxrank.data.user.UserData

data class UserDataUiState(
    val id: Int = 1,
    val timerStartTimeMillis: Long = 0,
    val timerStarted: Boolean = false,
    val rankPoints: Int = 0,
    val xpPoints: Int = 0,
    val timerDifficulty: TimerDifficulty = TimerDifficulty.Easy,
    val dailyTasksLastRefreshTime: Long = System.currentTimeMillis(),
    val weeklyTasksLastRefreshTime: Long = System.currentTimeMillis(),
    val monthlyTasksLastRefreshTime: Long = System.currentTimeMillis(),
    val tasksFinished: Int = 0,
    val pagesRead: Int = 0
)

fun UserDataUiState.toUserData(): UserData = UserData(
    id = id,
    timerStartTimeMillis = timerStartTimeMillis,
    timerStarted = timerStarted,
    rankPoints = rankPoints,
    xpPoints = xpPoints,
    timerDifficulty = timerDifficulty,
    dailyTasksLastRefreshTime = dailyTasksLastRefreshTime,
    weeklyTasksLastRefreshTime = weeklyTasksLastRefreshTime,
    monthlyTasksLastRefreshTime = monthlyTasksLastRefreshTime,
    tasksFinished = tasksFinished,
    pagesRead = pagesRead
)

fun UserData.toUserDataUiState(): UserDataUiState = UserDataUiState(
    id = id,
    timerStartTimeMillis = timerStartTimeMillis,
    timerStarted = timerStarted,
    rankPoints = rankPoints,
    xpPoints = xpPoints,
    timerDifficulty = timerDifficulty,
    dailyTasksLastRefreshTime = dailyTasksLastRefreshTime,
    weeklyTasksLastRefreshTime = weeklyTasksLastRefreshTime,
    monthlyTasksLastRefreshTime = monthlyTasksLastRefreshTime,
    tasksFinished = tasksFinished,
    pagesRead = pagesRead
)