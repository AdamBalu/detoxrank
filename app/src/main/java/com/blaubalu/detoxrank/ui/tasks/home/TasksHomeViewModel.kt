package com.blaubalu.detoxrank.ui.tasks.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blaubalu.detoxrank.data.task.Task
import com.blaubalu.detoxrank.data.task.TaskIconCategory
import com.blaubalu.detoxrank.data.task.TasksRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * View Model to retrieve all currently selected tasks from the database
 */
class TasksHomeViewModel(tasksRepository: TasksRepository) : ViewModel() {
    val tasksHomeUiState: StateFlow<TasksHomeUiState> = tasksRepository
        .getSelectedTasks()
        .map { TasksHomeUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = TasksHomeUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private val _firstTimeOpened = mutableStateOf(true)
    val firstTimeOpened: MutableState<Boolean>
        get() = _firstTimeOpened

    fun setFirstTimeOpened() {
        _firstTimeOpened.value = false
    }

    private val _availableRefreshes = mutableStateOf(0)
    val availableRefreshes: MutableState<Int>
        get() = _availableRefreshes

    private val _createTaskMenuShown = mutableStateOf(false)
    val createTaskMenuShown: Boolean
        get() = _createTaskMenuShown.value

    fun invertCreateTaskMenuShownValue() {
        _createTaskMenuShown.value = !_createTaskMenuShown.value
    }

    private var _createdTaskDescription = mutableStateOf("")
    val createdTaskDescription: MutableState<String>
        get() = _createdTaskDescription
    private var _createdTaskSelectedIcon = mutableStateOf(TaskIconCategory.Health)
    val createdTaskSelectedIcon: MutableState<TaskIconCategory>
        get() = _createdTaskSelectedIcon

    fun setCreatedTaskIcon(icon: TaskIconCategory) {
        _createdTaskSelectedIcon.value = icon
    }

    fun setCreatedTaskDescription(description: String) {
        _createdTaskDescription.value = description
    }
}

/**
 * Ui State for TasksHomeContent
 */
data class TasksHomeUiState(
    val taskList: List<Task> = listOf()
)