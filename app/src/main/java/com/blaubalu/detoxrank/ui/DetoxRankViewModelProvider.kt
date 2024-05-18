package com.blaubalu.detoxrank.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.blaubalu.detoxrank.DetoxRankApp
import com.blaubalu.detoxrank.ui.rank.AchievementViewModel
import com.blaubalu.detoxrank.ui.rank.RankViewModel
import com.blaubalu.detoxrank.ui.tasks.home.TasksHomeViewModel
import com.blaubalu.detoxrank.ui.tasks.task.TaskViewModel
import com.blaubalu.detoxrank.ui.theory.TheoryViewModel
import com.blaubalu.detoxrank.ui.timer.TimerViewModel

object DetoxRankViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            DetoxRankViewModel(
                detoxRankApplication(),
                tasksRepository = detoxRankApplication().container.tasksRepository,
                userDataRepository = detoxRankApplication().container.userDataRepository,
                achievementRepository = detoxRankApplication().container.achievementRepository
            )
        }

        initializer {
            RankViewModel(
                achievementRepository = detoxRankApplication().container.achievementRepository,
                userDataRepository = detoxRankApplication().container.userDataRepository
            )
        }

        initializer {
            TasksHomeViewModel(
                tasksRepository = detoxRankApplication().container.tasksRepository
            )
        }

        initializer {
            TaskViewModel(
                tasksRepository = detoxRankApplication().container.tasksRepository
            )
        }

        initializer {
            TheoryViewModel(
                chaptersRepository = detoxRankApplication().container.chaptersRepository
            )
        }

        initializer {
            TimerViewModel()
        }

        initializer {
            UserDataViewModel(
                userDataRepository = detoxRankApplication().container.userDataRepository
            )
        }

        initializer {
            AchievementViewModel(
                achievementRepository = detoxRankApplication().container.achievementRepository,
                userDataRepository = detoxRankApplication().container.userDataRepository
            )
        }
    }
}

fun CreationExtras.detoxRankApplication(): DetoxRankApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as DetoxRankApp)
