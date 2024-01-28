package com.blaubalu.detoxrank.data.achievements

import kotlinx.coroutines.flow.Flow

/**
 * Repository for Achievement entity
 */
class OfflineAchievementRepository(
    private val achievementDao: AchievementDao
) : AchievementRepository {
    override fun getAllAchievements(): Flow<List<Achievement>> = achievementDao.getAllAchievements()
    override fun getAchievementById(id: Int): Flow<Achievement?> = achievementDao.getById(id)

    override suspend fun update(achievement: Achievement) = achievementDao.update(achievement)
    override suspend fun insert(achievement: Achievement) = achievementDao.insert(achievement)
    override suspend fun delete(achievement: Achievement) = achievementDao.delete(achievement)

    override fun devCompleteAllAchievements() = achievementDao.devCompleteAllAchievements()

    override fun devUnCompleteAllAchievements() = achievementDao.devUnCompleteAllAchievements()
}
