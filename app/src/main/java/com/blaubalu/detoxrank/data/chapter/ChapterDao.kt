package com.blaubalu.detoxrank.data.chapter

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Data access object for Chapter entity
 */
@Dao
interface ChapterDao {
    @Insert
    suspend fun insert(chapter: Chapter)

    @Delete
    suspend fun delete(chapter: Chapter)

    @Update
    suspend fun update(chapter: Chapter)

    @Query("SELECT * FROM chapter WHERE name = :name")
    fun getChapterByName(name: String): Flow<Chapter?>

    @Query("SELECT * FROM chapter")
    fun getAllChapters(): Flow<List<Chapter>>

    @Query("SELECT * FROM chapter WHERE id = :id")
    fun getChapterById(id: Int): Flow<Chapter?>
}
