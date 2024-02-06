package com.blaubalu.detoxrank.ui

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blaubalu.detoxrank.data.Section
import com.blaubalu.detoxrank.data.TimerDifficulty
import com.blaubalu.detoxrank.data.achievements.AchievementRepository
import com.blaubalu.detoxrank.data.task.TaskDurationCategory
import com.blaubalu.detoxrank.data.task.TasksRepository
import com.blaubalu.detoxrank.data.user.Rank
import com.blaubalu.detoxrank.data.user.UserDataRepository
import com.blaubalu.detoxrank.ui.utils.Constants
import com.blaubalu.detoxrank.ui.utils.Constants.BRONZE_III_LOWER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.BRONZE_III_UPPER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.BRONZE_II_LOWER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.BRONZE_II_UPPER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.BRONZE_I_LOWER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.BRONZE_I_UPPER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.DIA_III_LOWER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.DIA_III_UPPER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.DIA_II_LOWER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.DIA_II_UPPER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.DIA_I_LOWER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.DIA_I_UPPER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.GOLD_III_LOWER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.GOLD_III_UPPER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.GOLD_II_LOWER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.GOLD_II_UPPER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.GOLD_I_LOWER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.GOLD_I_UPPER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.MASTER_LOWER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.MASTER_UPPER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.PLAT_III_LOWER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.PLAT_III_UPPER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.PLAT_II_LOWER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.PLAT_II_UPPER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.PLAT_I_LOWER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.PLAT_I_UPPER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.SILVER_III_LOWER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.SILVER_III_UPPER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.SILVER_II_LOWER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.SILVER_II_UPPER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.SILVER_I_LOWER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.SILVER_I_UPPER_CAP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.Calendar

/**
 * View Model for the main app. Handles task refreshes and rank icons with rank points.
 */
class DetoxRankViewModel(
    application: Application,
    private val userDataRepository: UserDataRepository,
    private val tasksRepository: TasksRepository,
    private val achievementRepository: AchievementRepository
) : AndroidViewModel(application) {
    private val sharedPrefs = application.getSharedPreferences(
        application.packageName + "_preferences",
        Context.MODE_PRIVATE
    )

    /** UI state exposed to the UI **/
    private val _uiState = MutableStateFlow(DetoxRankUiState())
    val uiState: StateFlow<DetoxRankUiState> = _uiState.asStateFlow()

    var userDataUiState by mutableStateOf(UserDataUiState())
        private set

    var achievementUiState by mutableStateOf(AchievementUiState())
        private set

    /**
     * Update [currentSection]
     */
    fun updateCurrentSection(section: Section) {
        _uiState.update {
            it.copy(
                currentSection = section
            )
        }
    }

    /**
     * Update [timerDifficulty]
     */
    fun setTimerDifficultyUiState(value: TimerDifficulty) {
        _uiState.update {
            it.copy(
                currentTimerDifficulty = value
            )
        }
    }

    fun setTimerDifficultyDatabase(value: TimerDifficulty) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userDataRepository.updateTimerDifficulty(value)
            }
        }
    }

    fun getRankProgressBarValue(): Float {
        return _uiState.value.rankProgressBarProgression
    }

    fun getLevelProgressBarValue(): Float {
        return _uiState.value.levelProgressBarProgression
    }

    fun getCurrentRank(): Rank {
        return _uiState.value.currentRank
    }

    fun getCurrentLevel(): Int {
        return _uiState.value.currentLevel
    }

    fun setRankProgressBar(value: Float) {
        _uiState.update {
            it.copy(
                rankProgressBarProgression = value
            )
        }
    }

    suspend fun getUserRankPoints(): Int {
        return userDataRepository.getUserStream().first().rankPoints
    }

    suspend fun getUserXpPoints(): Int {
        return userDataRepository.getUserStream().first().xpPoints
    }

    suspend fun getUserTasksRefreshedTimeInstance(taskDurationCategory: TaskDurationCategory): Long {
        return when (taskDurationCategory) {
            TaskDurationCategory.Daily -> {
                userDataRepository.getUserStream().first().dailyTasksLastRefreshTime
            }
            TaskDurationCategory.Weekly -> {
                userDataRepository.getUserStream().first().weeklyTasksLastRefreshTime
            }
            TaskDurationCategory.Monthly -> {
                userDataRepository.getUserStream().first().monthlyTasksLastRefreshTime
            }
            else -> {
                0
            }
        }
    }

    suspend fun firstRunGetTasks() {
        val firstRun = sharedPrefs.getBoolean("first_run", true)
        if (firstRun) {
            getNewTasksWithoutProgress(TaskDurationCategory.Daily, Constants.NUMBER_OF_TASKS_DAILY)
            getNewTasksWithoutProgress(TaskDurationCategory.Weekly,
                Constants.NUMBER_OF_TASKS_WEEKLY
            )
            getNewTasksWithoutProgress(TaskDurationCategory.Monthly,
                Constants.NUMBER_OF_TASKS_MONTHLY
            )
            withContext(Dispatchers.IO) {
                userDataRepository.updateDailyTasksLastRefreshTime(System.currentTimeMillis())
                userDataRepository.updateWeeklyTasksLastRefreshTime(System.currentTimeMillis())
                userDataRepository.updateMonthlyTasksLastRefreshTime(System.currentTimeMillis())
            }
            sharedPrefs.edit().putBoolean("first_run", false).apply()
        }
    }

    private suspend fun getNewTasksWithoutProgress(taskDurationCategory: TaskDurationCategory, numberOfTasks: Int) {
        tasksRepository.resetTasksFromCategory(durationCategory = taskDurationCategory)
        tasksRepository.selectNRandomTasksByDuration(taskDurationCategory, numberOfTasks)
    }

    private suspend fun getNewTasks(taskDurationCategory: TaskDurationCategory) {
        viewModelScope.launch {
            val time = System.currentTimeMillis()
            withContext(Dispatchers.IO) {
                when (taskDurationCategory) {
                    TaskDurationCategory.Daily -> {
                        userDataRepository.updateDailyTasksLastRefreshTime(time)
                    }
                    TaskDurationCategory.Weekly -> {
                        userDataRepository.updateWeeklyTasksLastRefreshTime(time)
                    }
                    TaskDurationCategory.Monthly -> {
                        userDataRepository.updateMonthlyTasksLastRefreshTime(time)
                    }
                    else -> {}
                }
                tasksRepository.getNewTasks(taskDurationCategory = taskDurationCategory)
            }
        }
    }

    suspend fun refreshTasks(calendarDaily: Calendar, calendarWeekly: Calendar, calendarMonthly: Calendar) {
        val day = calendarDaily.get(Calendar.DAY_OF_YEAR)
        val yearDaily = calendarDaily.get(Calendar.YEAR)

        // times of previously last refreshed weekly task
        val week = calendarWeekly.get(Calendar.WEEK_OF_YEAR)
        val yearWeekly = calendarWeekly.get(Calendar.YEAR)

        // times of previously last refreshed monthly task
        val month = calendarMonthly.get(Calendar.MONTH)
        val yearMonthly = calendarMonthly.get(Calendar.YEAR)

        val currentTime = Calendar.getInstance().apply {
            firstDayOfWeek = Calendar.MONDAY
        }

        currentTime.timeInMillis = System.currentTimeMillis()

        val currDayOfYear = currentTime.get(Calendar.DAY_OF_YEAR)
        val currWeekOfYear = currentTime.get(Calendar.WEEK_OF_YEAR)
        val currYear = currentTime.get(Calendar.YEAR)
        val currMonth = currentTime.get(Calendar.MONTH)

        val isFromYesterday = currYear > yearDaily || (currYear == yearDaily && currDayOfYear > day)
        val isFromLastWeek = currYear > yearDaily || (currYear == yearWeekly && currWeekOfYear > week)
        val isFromLastMonth = currYear > yearMonthly || (currYear == yearMonthly && currMonth > month)

        if (isFromYesterday)
            getNewTasks(TaskDurationCategory.Daily)
        if (isFromLastWeek)
            getNewTasks(TaskDurationCategory.Weekly)
        if (isFromLastMonth)
            getNewTasks(TaskDurationCategory.Monthly)
    }

    suspend fun updateUserData() {
        userDataRepository.updateUserData(userDataUiState.toUserData())
    }

    fun updateUiState(newDataUiState: UserDataUiState) {
        userDataUiState = newDataUiState.copy()
    }

    fun updateAchievementUiState(newAchievementUiState: AchievementUiState) {
        achievementUiState = newAchievementUiState.copy()
    }

    suspend fun insertAchievementToDatabase() {
        achievementRepository.insert(achievementUiState.toAchievement())
    }

    suspend fun updateUserRankPoints(toAdd: Int) {
        withContext(Dispatchers.IO) {
            userDataRepository.updateRankPoints(toAdd)
        }
    }

    suspend fun completeAchievement(achievementId: Int) {
        achievementRepository.getAchievementById(achievementId).collect { achievement ->
            if (achievement != null && !achievement.achieved)
                achievementRepository.update(achievement.copy(achieved = true))
        }
    }

    suspend fun getUserXPPoints(): Int {
        return userDataRepository.getUserStream().first().xpPoints
    }

    suspend fun getUserTimerDifficulty(): TimerDifficulty {
        return userDataRepository.getUserStream().first().timerDifficulty
    }

    suspend fun getUserTimerStarted(): Boolean {
        return userDataRepository.getUserStream().first().timerStarted
    }

    suspend fun updateUserXPPoints(toAdd: Int) {
        val user = userDataRepository.getUserStream().first()
        updateUiState(user.copy(xpPoints = user.xpPoints + toAdd).toUserDataUiState())
        updateUserData()
    }

    suspend fun updateTimerStartedTimeMillis() {
        withContext(Dispatchers.IO) {
            userDataRepository.updateTimerStartTimeMillis(System.currentTimeMillis())
        }
    }

    suspend fun updateTimerStarted(value: Boolean) {
        withContext(Dispatchers.IO) {
            userDataRepository.updateTimerStarted(value)
        }
    }

    fun setLevelProgressBar(value: Float) {
        _uiState.update {
            it.copy(
                levelProgressBarProgression = value
            )
        }
    }

    fun setCurrentLevel(value: Int) {
        _uiState.update {
            it.copy(
                currentLevel = value
            )
        }
    }

    fun setCurrentRank(rank: Rank) {
        _uiState.update {
            it.copy(
                currentRank = rank
            )
        }
    }

    fun setCurrentTimerDifficulty(timerDifficulty: TimerDifficulty) {
        _uiState.update {
            it.copy(
                currentTimerDifficulty = timerDifficulty
            )
        }
    }

    fun setTimerStarted(timerStarted: Boolean) {
        _uiState.update {
            it.copy(
                isTimerStarted = timerStarted
            )
        }
    }

    fun getCurrentRank(rankPoints: Int): Pair<Rank, Pair<Int, Int>> {
        return when (rankPoints) {
            in bronze1.first..bronze1.second -> Pair(Rank.Bronze1, bronze1)
            in bronze2.first..bronze2.second -> Pair(Rank.Bronze2, bronze2)
            in bronze3.first..bronze3.second -> Pair(Rank.Bronze3, bronze3)

            in silver1.first..silver1.second -> Pair(Rank.Silver1, silver1)
            in silver2.first..silver2.second -> Pair(Rank.Silver2, silver2)
            in silver3.first..silver3.second -> Pair(Rank.Silver3, silver3)

            in gold1.first..gold1.second -> Pair(Rank.Gold1, gold1)
            in gold2.first..gold2.second -> Pair(Rank.Gold2, gold2)
            in gold3.first..gold3.second -> Pair(Rank.Gold3, gold3)

            in platinum1.first..platinum1.second -> Pair(Rank.Platinum1, platinum1)
            in platinum2.first..platinum2.second -> Pair(Rank.Platinum2, platinum2)
            in platinum3.first..platinum3.second -> Pair(Rank.Platinum3, platinum3)

            in diamond1.first..diamond1.second -> Pair(Rank.Diamond1, diamond1)
            in diamond2.first..diamond2.second -> Pair(Rank.Diamond2, diamond2)
            in diamond3.first..diamond3.second -> Pair(Rank.Diamond3, diamond3)

            in master.first..master.second -> Pair(Rank.Master, master)
            else -> Pair(Rank.Legend, Pair(0, 0))
        }
    }

    companion object {
        private val bronze1 = Pair(BRONZE_I_LOWER_CAP, BRONZE_I_UPPER_CAP)
        private val bronze2 = Pair(BRONZE_II_LOWER_CAP, BRONZE_II_UPPER_CAP)
        private val bronze3 = Pair(BRONZE_III_LOWER_CAP, BRONZE_III_UPPER_CAP)

        private val silver1 = Pair(SILVER_I_LOWER_CAP, SILVER_I_UPPER_CAP)
        private val silver2 = Pair(SILVER_II_LOWER_CAP, SILVER_II_UPPER_CAP)
        private val silver3 = Pair(SILVER_III_LOWER_CAP, SILVER_III_UPPER_CAP)

        private val gold1 = Pair(GOLD_I_LOWER_CAP, GOLD_I_UPPER_CAP)
        private val gold2 = Pair(GOLD_II_LOWER_CAP, GOLD_II_UPPER_CAP)
        private val gold3 = Pair(GOLD_III_LOWER_CAP, GOLD_III_UPPER_CAP)

        private val platinum1 = Pair(PLAT_I_LOWER_CAP, PLAT_I_UPPER_CAP)
        private val platinum2 = Pair(PLAT_II_LOWER_CAP, PLAT_II_UPPER_CAP)
        private val platinum3 = Pair(PLAT_III_LOWER_CAP, PLAT_III_UPPER_CAP)

        private val diamond1 = Pair(DIA_I_LOWER_CAP, DIA_I_UPPER_CAP)
        private val diamond2 = Pair(DIA_II_LOWER_CAP, DIA_II_UPPER_CAP)
        private val diamond3 = Pair(DIA_III_LOWER_CAP, DIA_III_UPPER_CAP)

        private val master = Pair(MASTER_LOWER_CAP, MASTER_UPPER_CAP)
    }
}
