package com.blaubalu.detoxrank.data.task

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Data access object for Task entity
 */
@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Update
    suspend fun update(task: Task)

    @Query("SELECT * FROM task WHERE duration_category = :taskDurationCategory AND completed = 1")
    fun getCompletedTasksByDuration(taskDurationCategory: TaskDurationCategory): Flow<List<Task>>

    @Query("SELECT * FROM task")
    fun getAllTasks(): Flow<List<Task>>

    @Transaction
    @Query("UPDATE task SET selected = 1 WHERE duration_category = :durationCategory AND id IN (SELECT id FROM task WHERE duration_category = :durationCategory AND was_selected_last_time = 0 AND selected = 0 ORDER BY RANDOM() LIMIT :numberOfTasks)")
    suspend fun selectNRandomTasksByDuration(durationCategory: TaskDurationCategory,
                                             numberOfTasks: Int)
    @Transaction
    @Query("UPDATE task SET selected = 0, completed = 0 WHERE duration_category = :durationCategory")
    suspend fun resetTasksFromCategory(durationCategory: TaskDurationCategory)

    @Query("SELECT * FROM task WHERE selected = 1")
    fun getSelectedTasks(): Flow<List<Task>>

    @Query("SELECT COUNT(*) FROM task WHERE duration_category = :taskDurationCategory AND completed = 1")
    fun getCompletedTaskNum(taskDurationCategory: TaskDurationCategory): Int

    @Query("UPDATE task SET selected = 1 WHERE duration_category = \"Special\"")
    fun selectSpecialTasks()

    @Update
    suspend fun updateRows(taskRows: List<Task>)

    @Transaction
    @Query("UPDATE task SET was_selected_last_time = :value WHERE selected = 1 AND duration_category = :taskDurationCategory")
    fun updateTasksSelectedLastTime(taskDurationCategory: TaskDurationCategory, value: Boolean)

    @Transaction
    @Query("UPDATE task SET was_selected_last_time = 0 WHERE duration_category = :taskDurationCategory AND was_selected_last_time = 1")
    fun resetSelectedLastTime(taskDurationCategory: TaskDurationCategory)
}