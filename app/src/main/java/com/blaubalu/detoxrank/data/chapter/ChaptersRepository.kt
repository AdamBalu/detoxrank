package com.blaubalu.detoxrank.data.chapter

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Chapter entity
 */
interface ChaptersRepository {
    fun getChapterByName(name: String): Flow<Chapter?>
    fun getChapterById(id: Int): Flow<Chapter?>
    suspend fun insertChapter(chapter: Chapter)
    suspend fun deleteChapter(chapter: Chapter)
    suspend fun updateChapter(chapter: Chapter)
    fun getAllChapters(): Flow<List<Chapter>>
}
