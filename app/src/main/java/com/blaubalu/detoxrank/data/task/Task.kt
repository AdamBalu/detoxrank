package com.blaubalu.detoxrank.data.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.blaubalu.detoxrank.data.Converters

enum class TaskDurationCategory {
    Uncategorized, Special, Daily, Weekly, Monthly;
    override fun toString(): String {
        return when (this) {
            Uncategorized -> ""
            Special -> "Special"
            Daily -> "Daily"
            Weekly -> "Weekly"
            Monthly -> "Monthly"
        }
    }
}

enum class TaskIconCategory {
    Exercise,
    Health,
    Running,
    Walking,
    Swimming,
    Meditation,
    HealthyFood,
    UnhealthyFood,
    Cleaning,
    Cycling,
    Reading,
    Hiking,
    Fun,
    Places,
    Creating,
    Music,
    Sports,
    Photography,
    Other
}

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String,
    var completed: Boolean = false,
    @ColumnInfo(name = "duration_category")
    @TypeConverters(Converters::class)
    val durationCategory: TaskDurationCategory,
    @ColumnInfo(name = "icon_category")
    @TypeConverters(Converters::class)
    val iconCategory: TaskIconCategory,
    @ColumnInfo(name = "selected")
    var selectedAsCurrentTask: Boolean = false,
    val language: String = "EN",
    @ColumnInfo(name = "special_task_id")
    val specialTaskID: Int = 0,
    @ColumnInfo(name = "was_selected_last_time")
    val wasSelectedLastTime: Boolean = false
)
