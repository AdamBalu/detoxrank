package com.blaubalu.detoxrank.data.user

import com.blaubalu.detoxrank.data.TimerDifficulty
import kotlinx.coroutines.flow.Flow

/**
 * Repository for UserData entity
 */
class OfflineUserDataRepository(private val userDataDao: UserDataDao) : UserDataRepository {
    override suspend fun updateUserData(userData: UserData) = userDataDao.update(userData)
    override fun getUserStream(): Flow<UserData> = userDataDao.getUser()
    override suspend fun insertUserData(userData: UserData) = userDataDao.insert(userData)

    override fun updatePagesRead(amount: Int) = userDataDao.updatePagesRead(amount)
    override fun updateRankPoints(amount: Int) = userDataDao.updateRankPoints(amount)
    override fun updateXpPoints(amount: Int) = userDataDao.updateXpPoints(amount)
    override fun updateTimerStartTimeMillis(time: Long) = userDataDao.updateTimerStartTimeMillis(time)
    override fun updateTimerStarted(value: Boolean) = userDataDao.updateTimerStarted(value)
    override fun updateTimerDifficulty(value: TimerDifficulty) = userDataDao.updateTimerDifficulty(value)
    override fun updateDailyTasksLastRefreshTime(time: Long) = userDataDao.updateDailyTasksLastRefreshTime(time)
    override fun updateWeeklyTasksLastRefreshTime(time: Long) = userDataDao.updateWeeklyTasksLastRefreshTime(time)
    override fun updateMonthlyTasksLastRefreshTime(time: Long) = userDataDao.updateMonthlyTasksLastRefreshTime(time)
}