package com.blaubalu.detoxrank.ui.rank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blaubalu.detoxrank.data.achievements.AchievementRepository
import com.blaubalu.detoxrank.data.user.UserDataRepository
import com.blaubalu.detoxrank.ui.utils.Constants
import com.blaubalu.detoxrank.ui.utils.Constants.BOOKS_10_PAGE_COUNT
import com.blaubalu.detoxrank.ui.utils.Constants.BOOKS_1_PAGE_COUNT
import com.blaubalu.detoxrank.ui.utils.Constants.BOOKS_5_PAGE_COUNT
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_ALL_CH
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_CH_1
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_CH_2
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_CH_3
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_CH_4
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_CH_5
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_CH_6
import com.blaubalu.detoxrank.ui.utils.Constants.ID_READ_100_PAGES
import com.blaubalu.detoxrank.ui.utils.Constants.ID_READ_10_BOOKS
import com.blaubalu.detoxrank.ui.utils.Constants.ID_READ_20_PAGES
import com.blaubalu.detoxrank.ui.utils.Constants.ID_READ_250_PAGES
import com.blaubalu.detoxrank.ui.utils.Constants.ID_READ_50_PAGES
import com.blaubalu.detoxrank.ui.utils.Constants.ID_READ_5_BOOKS
import com.blaubalu.detoxrank.ui.utils.Constants.ID_READ_A_BOOK
import com.blaubalu.detoxrank.ui.utils.Constants.PAGES_100
import com.blaubalu.detoxrank.ui.utils.Constants.PAGES_20
import com.blaubalu.detoxrank.ui.utils.Constants.PAGES_250
import com.blaubalu.detoxrank.ui.utils.Constants.PAGES_50
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AchievementViewModel(
    private val achievementRepository: AchievementRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    /**
     * Completes the All Chapters Finished achievement if all of the chapters are finished
     */
    suspend fun finishAllChaptersAchievement() {
        val ch1 = achievementRepository.getAchievementById(ID_FINISH_CH_1).first()
        val ch2 = achievementRepository.getAchievementById(ID_FINISH_CH_2).first()
        val ch3 = achievementRepository.getAchievementById(ID_FINISH_CH_3).first()
        val ch4 = achievementRepository.getAchievementById(ID_FINISH_CH_4).first()
        val ch5 = achievementRepository.getAchievementById(ID_FINISH_CH_5).first()
        val ch6 = achievementRepository.getAchievementById(ID_FINISH_CH_6).first()
        if (ch1?.achieved == true &&
            ch2?.achieved == true &&
            ch3?.achieved == true &&
            ch4?.achieved == true &&
            ch5?.achieved == true &&
            ch6?.achieved == true) {
            val achievement = achievementRepository.getAchievementById(ID_FINISH_ALL_CH).first()
            if ((achievement != null) && !achievement.achieved) {
                achievementRepository.update(
                    achievement = achievement.copy(
                        achieved = true
                    )
                )
            }
        }
    }

    /**
     * Finishes achievements related to book reading
     */
    suspend fun finishBookReadingAchievements() {
        val pagesRead = userDataRepository.getUserStream().first().pagesRead
        if (pagesRead >= PAGES_20)
            achieveAchievement(ID_READ_20_PAGES)
        if (pagesRead >= PAGES_50)
            achieveAchievement(ID_READ_50_PAGES)
        if (pagesRead >= PAGES_100)
            achieveAchievement(ID_READ_100_PAGES)
        if (pagesRead >= PAGES_250)
            achieveAchievement(ID_READ_250_PAGES)
        if (pagesRead >= BOOKS_1_PAGE_COUNT)
            achieveAchievement(ID_READ_A_BOOK)
        if (pagesRead >= BOOKS_5_PAGE_COUNT)
            achieveAchievement(ID_READ_5_BOOKS)
        if (pagesRead >= BOOKS_10_PAGE_COUNT)
            achieveAchievement(ID_READ_10_BOOKS)
    }

    /**
     * Updates the achieved state of a provided achievement
     * @param id id of an achievement to achieve
     */
    suspend fun achieveAchievement(id: Int) {
        val achievement = achievementRepository.getAchievementById(id).first()
        if ((achievement != null) && !achievement.achieved) {
            achievementRepository.update(
                achievement = achievement.copy(
                    achieved = true
                )
            )
        }
    }

    /**
     * Achieves achievements related to timer
     */
    suspend fun achieveTimerAchievements(days: Int) {
        if (days >= 3)
            achieveAchievement(Constants.ID_TIMER_3_DAYS)
        if (days >= 7)
            achieveAchievement(Constants.ID_TIMER_7_DAYS)
        if (days >= 14)
            achieveAchievement(Constants.ID_TIMER_14_DAYS)
        if (days >= 30)
            achieveAchievement(Constants.ID_TIMER_30_DAYS)
    }

    suspend fun deleteAllAchievementsInDb() {
        val allAchievements = achievementRepository.getAllAchievements()
        allAchievements.collect { achievements ->
            for (achievement in achievements) {
                achievementRepository.delete(achievement)
            }
        }
    }

    fun devCompleteAllAchievements() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                achievementRepository.devCompleteAllAchievements()
            }
        }
    }

    fun devUnCompleteAllAchievements() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                achievementRepository.devUnCompleteAllAchievements()
            }
        }
    }
}