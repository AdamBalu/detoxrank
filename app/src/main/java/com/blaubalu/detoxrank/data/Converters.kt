package com.blaubalu.detoxrank.data

import androidx.room.TypeConverter
import com.blaubalu.detoxrank.data.achievements.AchievementDifficulty
import com.blaubalu.detoxrank.data.chapter.ChapterDifficulty
import com.blaubalu.detoxrank.data.chapter.ChapterTag
import com.blaubalu.detoxrank.data.task.TaskDurationCategory
import com.blaubalu.detoxrank.data.task.TaskIconCategory

/**
 * Converts the non-trivial data types to setup for database insertion and extraction
 */
class Converters {
    @TypeConverter
    fun fromTaskDurationCategory(durationCategory: TaskDurationCategory): String = durationCategory.name
    @TypeConverter
    fun toTaskCategory(durationCategory: String): TaskDurationCategory = TaskDurationCategory.valueOf(durationCategory)
    @TypeConverter
    fun fromTaskIconCategory(iconCategory: TaskIconCategory): String = iconCategory.name
    @TypeConverter
    fun toTaskIconCategory(iconCategory: String): TaskIconCategory = TaskIconCategory.valueOf(iconCategory)

    @TypeConverter
    fun fromChapterDifficulty(difficulty: ChapterDifficulty): String = difficulty.name
    @TypeConverter
    fun toChapterDifficulty(difficulty: String): ChapterDifficulty = ChapterDifficulty.valueOf(difficulty)
    @TypeConverter
    fun fromChapterTag(tag: ChapterTag): String = tag.name
    @TypeConverter
    fun toChapterTag(tag: String): ChapterTag = ChapterTag.valueOf(tag)

    @TypeConverter
    fun fromTimerDifficulty(difficulty: TimerDifficulty): String = difficulty.name
    @TypeConverter
    fun toTimerDifficulty(difficulty: String): TimerDifficulty = TimerDifficulty.valueOf(difficulty)

    @TypeConverter
    fun fromAchievementDifficulty(difficulty: AchievementDifficulty): String = difficulty.name
    @TypeConverter
    fun toAchievementDifficulty(difficulty: String): AchievementDifficulty = AchievementDifficulty.valueOf(difficulty)
}