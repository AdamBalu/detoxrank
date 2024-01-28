package com.blaubalu.detoxrank.ui

import com.blaubalu.detoxrank.data.Section
import com.blaubalu.detoxrank.data.TimerDifficulty
import com.blaubalu.detoxrank.data.user.Rank

data class DetoxRankUiState(
    val currentSection: Section = Section.Rank,
    var progressBarProgression: Float = 0.0f,
    val currentTimerDifficulty: TimerDifficulty = TimerDifficulty.Easy,
    val rankProgressBarProgression: Float = 0.0f,
    val levelProgressBarProgression: Float = 0.0f,
    val currentLevel: Int = 1,
    val currentRank: Rank = Rank.Bronze1,
    val isTimerStarted: Boolean = false
)
