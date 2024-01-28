package com.blaubalu.detoxrank.data.chapter

import kotlinx.coroutines.flow.Flow

/**
 * Repository for Chapter entity
 */
class OfflineChaptersRepository(private val chaptersDao: ChapterDao) : ChaptersRepository {
    override fun getChapterByName(name: String): Flow<Chapter?> = chaptersDao.getChapterByName(name)
    override fun getChapterById(id: Int): Flow<Chapter?> = chaptersDao.getChapterById(id)

    override suspend fun insertChapter(chapter: Chapter) = chaptersDao.insert(chapter)

    override suspend fun deleteChapter(chapter: Chapter) = chaptersDao.delete(chapter)

    override suspend fun updateChapter(chapter: Chapter) = chaptersDao.update(chapter)

    override fun getAllChapters(): Flow<List<Chapter>> = chaptersDao.getAllChapters()
}