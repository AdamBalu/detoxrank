package com.blaubalu.detoxrank.data

import androidx.annotation.DrawableRes

enum class TimerDifficulty {
    Easy, Medium, Hard
}

data class TimerDifficultyCard(
    val difficulty: TimerDifficulty,
    val avoidList: List<Int>,
    @DrawableRes val backgroundImageRes: Int
)
