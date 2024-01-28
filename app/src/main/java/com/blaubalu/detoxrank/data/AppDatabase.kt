package com.blaubalu.detoxrank.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import com.blaubalu.detoxrank.data.achievements.Achievement
import com.blaubalu.detoxrank.data.achievements.AchievementDao
import com.blaubalu.detoxrank.data.chapter.Chapter
import com.blaubalu.detoxrank.data.chapter.ChapterDao
import com.blaubalu.detoxrank.data.task.Task
import com.blaubalu.detoxrank.data.task.TaskDao
import com.blaubalu.detoxrank.data.user.UserData
import com.blaubalu.detoxrank.data.user.UserDataDao

/**
 * Initializes the entire application Room database
 */
@Database(entities = [Task::class, Chapter::class, UserData::class, Achievement::class], version = 14)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun chapterDao(): ChapterDao
    abstract fun userDataDao(): UserDataDao
    abstract fun achievementDao(): AchievementDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .createFromAsset("database/app_database.db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}