package com.blaubalu.detoxrank.ui.rank

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blaubalu.detoxrank.data.achievements.Achievement
import com.blaubalu.detoxrank.data.achievements.AchievementRepository
import com.blaubalu.detoxrank.data.user.UserData
import com.blaubalu.detoxrank.data.user.UserDataRepository
import com.blaubalu.detoxrank.ui.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class RankViewModel(
    achievementRepository: AchievementRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {
    val achievementsHomeUiState: StateFlow<AchievementHomeUiState> = achievementRepository
        .getAllAchievements()
        .map { AchievementHomeUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = AchievementHomeUiState()
        )

    private val _rankPoints = mutableStateOf(0)
    val rankPoints: MutableState<Int>
        get() = _rankPoints

    private val _currentRankBounds = mutableStateOf(Pair(0, 0))
    val currentRankBounds: MutableState<Pair<Int, Int>>
        get() = _currentRankBounds

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private val _achievementsDisplayed = mutableStateOf(false)
    val achievementsDisplayed: MutableState<Boolean>
        get() = _achievementsDisplayed

    private val _helpDisplayed = mutableStateOf(false)
    val helpDisplayed: MutableState<Boolean>
        get() = _helpDisplayed

    fun setAchievementsDisplayed(isDisplayed: Boolean) {
        _achievementsDisplayed.value = isDisplayed
    }

    fun setHelpDisplayed(isDisplayed: Boolean) {
        _helpDisplayed.value = isDisplayed
    }

    suspend fun setLocalRankPoints() {
        val userData: UserData
        withContext(Dispatchers.IO) {
            userData = userDataRepository.getUserStream().first()
        }
        _rankPoints.value = userData.rankPoints
    }

    fun setLocalRankBounds(bounds: Pair<Int, Int>) {
        _currentRankBounds.value = bounds
    }

    /**
     * Formats the rank points progress for the label of rank linear progress bar
     * @return formatted rank points progress
     */
    fun getFormattedRankPointsProgress(): String {
        if (rankPoints.value >= Constants.LEGEND_LOWER_CAP)
            return "M A X"
        return "${rankPoints.value - currentRankBounds.value.first} / ${
            currentRankBounds.value.second + 1 - currentRankBounds.value.first
        }"
    }
}

data class AchievementHomeUiState(
    val achievementList: List<Achievement> = listOf()
)