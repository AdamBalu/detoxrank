package com.blaubalu.detoxrank.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
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
@Database(
    entities = [Task::class, Chapter::class, UserData::class, Achievement::class],
    version = 17,
    autoMigrations = [
        AutoMigration(from = 14, to = 15),
        AutoMigration(from = 15, to = 16),
        AutoMigration(from = 16, to = 17, spec = AppDatabase.MigrationFrom16To17::class)
    ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun chapterDao(): ChapterDao
    abstract fun userDataDao(): UserDataDao
    abstract fun achievementDao(): AchievementDao

    @DeleteColumn.Entries(DeleteColumn("user_data", "was_task_list_opened"))
    class MigrationFrom16To17: AutoMigrationSpec

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
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}