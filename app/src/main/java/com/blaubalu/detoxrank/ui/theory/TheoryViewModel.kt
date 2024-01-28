package com.blaubalu.detoxrank.ui.theory

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blaubalu.detoxrank.data.chapter.Chapter
import com.blaubalu.detoxrank.data.chapter.ChaptersRepository
import com.blaubalu.detoxrank.ui.DetoxRankUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TheoryViewModel(
    private val chaptersRepository: ChaptersRepository
) : ViewModel() {
    val theoryHomeUiState: StateFlow<TheoryHomeUiState> = chaptersRepository
        .getAllChapters()
        .map { TheoryHomeUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = TheoryHomeUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private val chapterId: Int = 0

    var chapterUiState by mutableStateOf(ChapterUiState())
        private set

    init {
        viewModelScope.launch {
            chapterUiState = chaptersRepository.getChapterById(chapterId)
                .filterNotNull()
                .first()
                .toChapterUiState()
        }
    }

    fun updateUiState(newChapterUiState: ChapterUiState) {
        chapterUiState = newChapterUiState.copy()
    }

    suspend fun insertChapterToChapterDatabase() {
        chaptersRepository.insertChapter(chapterUiState.toChapter())
    }

    // migrated
    private val _uiState = MutableStateFlow(DetoxRankUiState())
    private val _currentChapterScreenNum = mutableStateOf(0)
    val currentChapterScreenNum: MutableState<Int>
        get() = _currentChapterScreenNum

    private val _currentChapterName = mutableStateOf("")
    val currentChapterName: MutableState<String>
        get() = _currentChapterName
//    val uiState: StateFlow<DetoxRankUiState> = _uiState.asStateFlow()

    fun setCurrentChapterName(name: String) {
        _currentChapterName.value = name
    }
    fun getChapterByName(name: String) = chaptersRepository.getChapterByName(name)

    suspend fun setCurrentChapterScreenNum() {
        getChapterByName(_currentChapterName.value).collect {
            _currentChapterScreenNum.value = it?.screenNum ?: 0
        }
    }

    fun setChapterCompletionValue(chapter: Chapter?) {
        if (chapter == null)
            return
        viewModelScope.launch {
            chaptersRepository.updateChapter(chapter.copy(wasCompleted = true))
        }
    }

    fun getProgressBarValue(): Float {
        return _uiState.value.progressBarProgression
    }

    fun updateProgressBarProgression(valueToAdd: Float) {
        val progression = getProgressBarValue() + valueToAdd
        _uiState.update {
            it.copy(
                progressBarProgression = progression
            )
        }
    }

    fun resetProgressBarProgression() {
        _uiState.update {
            it.copy(
                progressBarProgression = 0f
            )
        }
    }

    fun calculateProgressBarAddition(screenNum: Int): Float =
        if (screenNum != 0)
            (1 / (screenNum - 1).toFloat())
        else 0f
}

/**
 * Ui State for TasksHomeContent
 */
data class TheoryHomeUiState(val chapterList: List<Chapter> = listOf())