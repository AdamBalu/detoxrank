package com.blaubalu.detoxrank.data.user

import com.blaubalu.detoxrank.data.TimerDifficulty
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for UserData entity
 */
interface UserDataRepository {
    suspend fun updateUserData(userData: UserData)
    suspend fun insertUserData(userData: UserData)
    fun getUserStream(): Flow<UserData>
    fun updatePagesRead(amount: Int)
    fun updateRankPoints(amount: Int)
    fun updateXpPoints(amount: Int)
    fun updateTimerStartTimeMillis(time: Long)
    fun updateTimerStarted(value: Boolean)
    fun updateTimerDifficulty(value: TimerDifficulty)
    fun updateDailyTasksLastRefreshTime(time: Long)
    fun updateWeeklyTasksLastRefreshTime(time: Long)
    fun updateMonthlyTasksLastRefreshTime(time: Long)
}
