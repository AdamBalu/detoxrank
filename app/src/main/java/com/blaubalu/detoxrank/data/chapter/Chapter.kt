package com.blaubalu.detoxrank.data.chapter

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.blaubalu.detoxrank.data.Converters

enum class ChapterDifficulty {
    Easy, Medium, Hard
}

enum class ChapterTag {
    Introduction, Dopamine, Reinforcement, Tolerance, HedonicCircuit, Solutions
}

@Entity(tableName = "chapter")
data class Chapter(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    @TypeConverters(Converters::class)
    val difficulty: ChapterDifficulty,
    var wasCompleted: Boolean = false,
    @TypeConverters(Converters::class)
    val tag: ChapterTag,
    val screenNum: Int,
    val startChapterButtonLabel: String,
    val language: String = "EN"
)
