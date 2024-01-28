package com.blaubalu.detoxrank.data

import android.content.Context
import com.blaubalu.detoxrank.data.achievements.AchievementRepository
import com.blaubalu.detoxrank.data.achievements.OfflineAchievementRepository
import com.blaubalu.detoxrank.data.chapter.ChaptersRepository
import com.blaubalu.detoxrank.data.chapter.OfflineChaptersRepository
import com.blaubalu.detoxrank.data.task.OfflineTasksRepository
import com.blaubalu.detoxrank.data.task.TasksRepository
import com.blaubalu.detoxrank.data.user.OfflineUserDataRepository
import com.blaubalu.detoxrank.data.user.UserDataRepository

interface AppContainer {
    val tasksRepository: TasksRepository
    val chaptersRepository: ChaptersRepository
    val userDataRepository: UserDataRepository
    val achievementRepository: AchievementRepository
}

class AppDataContainer(private val context: Context): AppContainer {
    override val tasksRepository: TasksRepository by lazy {
        OfflineTasksRepository(
            AppDatabase.getDatabase(context).taskDao(),
            OfflineAchievementRepository(AppDatabase.getDatabase(context).achievementDao()),
            OfflineUserDataRepository(AppDatabase.getDatabase(context).userDataDao())
        )
    }
    override val chaptersRepository: ChaptersRepository by lazy {
        OfflineChaptersRepository(AppDatabase.getDatabase(context).chapterDao())
    }
    override val userDataRepository: UserDataRepository by lazy {
        OfflineUserDataRepository(AppDatabase.getDatabase(context).userDataDao())
    }
    override val achievementRepository: AchievementRepository by lazy {
        OfflineAchievementRepository(AppDatabase.getDatabase(context).achievementDao())
    }
}