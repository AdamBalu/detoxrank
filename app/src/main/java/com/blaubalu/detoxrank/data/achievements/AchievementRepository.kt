package com.blaubalu.detoxrank.data.achievements

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Achievement entity
 */
interface AchievementRepository {
    fun getAllAchievements(): Flow<List<Achievement>>

    suspend fun update(achievement: Achievement)

    suspend fun insert(achievement: Achievement)
    suspend fun delete(achievement: Achievement)

    fun getAchievementById(id: Int): Flow<Achievement?>

    fun devCompleteAllAchievements()

    fun devUnCompleteAllAchievements()
}