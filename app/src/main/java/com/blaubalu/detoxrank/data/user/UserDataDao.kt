package com.blaubalu.detoxrank.data.user

import androidx.room.*
import com.blaubalu.detoxrank.data.TimerDifficulty
import kotlinx.coroutines.flow.Flow

/**
 * Data access object for UserData entity
 */
@Dao
interface UserDataDao {
    @Update
    suspend fun update(userData: UserData)

    @Insert
    suspend fun insert(userData: UserData)

    @Query("SELECT * FROM user_data")
    fun getUser(): Flow<UserData>

    @Transaction
    @Query("UPDATE user_data SET pages_read = pages_read + :amount WHERE id = 1")
    fun updatePagesRead(amount: Int)

    @Query("UPDATE user_data SET monthly_tasks_last_refresh_time = :time WHERE id = 1")
    fun updateMonthlyTasksLastRefreshTime(time: Long)

    @Query("UPDATE user_data SET weekly_tasks_last_refresh_time = :time WHERE id = 1")
    fun updateWeeklyTasksLastRefreshTime(time: Long)

    @Query("UPDATE user_data SET daily_tasks_last_refresh_time = :time WHERE id = 1")
    fun updateDailyTasksLastRefreshTime(time: Long)

    @Transaction
    @Query("UPDATE user_data SET rank_points = rank_points + :amount WHERE id = 1")
    fun updateRankPoints(amount: Int)

    @Transaction
    @Query("UPDATE user_data SET xp_points = xp_points + :amount WHERE id = 1")
    fun updateXpPoints(amount: Int)

    @Transaction
    @Query("UPDATE user_data SET timer_start_time = :time WHERE id = 1")
    fun updateTimerStartTimeMillis(time: Long)

    @Transaction
    @Query("UPDATE user_data SET timer_started = :value WHERE id = 1")
    fun updateTimerStarted(value: Boolean)

    @Query("UPDATE user_data SET timer_difficulty = :value WHERE id = 1")
    fun updateTimerDifficulty(value: TimerDifficulty)
}