package com.blaubalu.detoxrank.ui.theory

import com.blaubalu.detoxrank.data.chapter.Chapter
import com.blaubalu.detoxrank.data.chapter.ChapterDifficulty
import com.blaubalu.detoxrank.data.chapter.ChapterTag

data class ChapterUiState(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val difficulty: ChapterDifficulty = ChapterDifficulty.Easy,
    var wasCompleted: Boolean = false,
    val tag: ChapterTag = ChapterTag.Introduction,
    val screenNum: Int = 0,
    val startChapterButtonLabel: String = "",
    val language: String = "EN"
)

fun ChapterUiState.toChapter(): Chapter = Chapter(
    id = id,
    name = name,
    description = description,
    difficulty = difficulty,
    wasCompleted = wasCompleted,
    tag = tag,
    screenNum = screenNum,
    startChapterButtonLabel = startChapterButtonLabel,
    language = language
)

fun Chapter.toChapterUiState(): ChapterUiState = ChapterUiState(
    id = id,
    name = name,
    description = description,
    difficulty = difficulty,
    wasCompleted = wasCompleted,
    tag = tag,
    screenNum = screenNum,
    startChapterButtonLabel = startChapterButtonLabel,
    language = language
)