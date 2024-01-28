package com.blaubalu.detoxrank.data.achievements

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Data access object for Achievement entity
 */
@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievement")
    fun getAllAchievements(): Flow<List<Achievement>>

    @Update
    suspend fun update(achievement: Achievement)

    @Insert
    suspend fun insert(achievement: Achievement)

    @Delete
    suspend fun delete(achievement: Achievement)

    @Query("SELECT * FROM achievement WHERE id = :id")
    fun getById(id: Int): Flow<Achievement?>

    @Query("UPDATE achievement SET achieved = 1 WHERE achieved = 0")
    fun devCompleteAllAchievements()

    @Query("UPDATE achievement SET achieved = 0 WHERE achieved = 1")
    fun devUnCompleteAllAchievements()
}
