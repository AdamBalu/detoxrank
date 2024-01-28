package com.blaubalu.detoxrank.data.achievements

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.blaubalu.detoxrank.data.Converters

enum class AchievementDifficulty {
    EASY,
    MEDIUM,
    HARD,
    VERY_HARD,
    INSANE,
    LEGENDARY
}

@Entity(tableName = "achievement")
data class Achievement(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val achieved: Boolean = false,
    val name: String,
    val description: String = "",
    @TypeConverters(Converters::class)
    val difficulty: AchievementDifficulty
)