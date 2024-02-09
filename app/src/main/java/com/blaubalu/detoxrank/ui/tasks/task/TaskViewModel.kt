package com.blaubalu.detoxrank.ui.tasks.task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blaubalu.detoxrank.data.task.Task
import com.blaubalu.detoxrank.data.task.TaskDurationCategory
import com.blaubalu.detoxrank.data.task.TasksRepository
import com.blaubalu.detoxrank.data.user.UserDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskViewModel(
    private val tasksRepository: TasksRepository
    ) : ViewModel() {
    val wereTasksOpened = mutableStateOf(false)

    var taskUiState by mutableStateOf(TaskUiState())
        private set

    fun updateUiState(newTaskUiState: TaskUiState) {
        taskUiState = newTaskUiState.copy()
    }

    private val _taskList = mutableStateListOf<Task>()
    val taskList: List<Task>
        get() = _taskList

    suspend fun updateTask() {
        if (taskUiState.isValid())
            tasksRepository.updateTask(taskUiState.toTask())
    }

    suspend fun refreshTask(taskDurationCategory: TaskDurationCategory) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                tasksRepository.refreshTask(taskDurationCategory)
            }
        }
    }

    fun selectSpecialTasks() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                tasksRepository.selectSpecialTasks()
            }
        }
    }

    fun getCompletedTasksByDuration(taskDurationCategory: TaskDurationCategory): Flow<List<Task>> {
        return tasksRepository.getCompletedTasksByDuration(taskDurationCategory = taskDurationCategory)
    }

    suspend fun insertTaskToDatabase() {
        if (taskUiState.isValid()) {
            tasksRepository.insertTask(taskUiState.toTask())
        }
    }

    suspend fun deleteTask(task: Task) {
        delay(600)
        tasksRepository.deleteTask(task)
    }

    suspend fun deleteAllTasksInDb() {
        val allTasks = tasksRepository.getAllTasksStream()
        allTasks.collect { tasks ->
            for (task in tasks) {
                tasksRepository.deleteTask(task)
            }
        }
    }
}