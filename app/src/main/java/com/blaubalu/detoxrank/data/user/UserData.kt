package com.blaubalu.detoxrank.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.blaubalu.detoxrank.data.Converters
import com.blaubalu.detoxrank.data.TimerDifficulty

enum class UiTheme {
    Default, Light, Dark, Monochrome, GreenShades, BlueShades
}

enum class Rank(val rankName: String) {
    Bronze1("Bronze I"), Bronze2("Bronze II"), Bronze3("Bronze III"),
    Silver1("Silver I"), Silver2("Silver II"), Silver3("Silver III"),
    Gold1("Gold I"), Gold2("Gold II"), Gold3("Gold III"),
    Platinum1("Platinum I"), Platinum2("Platinum II"), Platinum3("Platinum III"),
    Diamond1("Diamond I"), Diamond2("Diamond II"), Diamond3("Diamond III"),
    Master("Master"),
    Legend("Legend")
}

@Entity(tableName = "user_data")
data class UserData(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    @ColumnInfo(name = "timer_start_time")
    val timerStartTimeMillis: Long = 0,
    @ColumnInfo(name = "timer_started")
    val timerStarted: Boolean = false,
    @ColumnInfo(name = "rank_points")
    val rankPoints: Int = 0,
    @ColumnInfo(name = "xp_points")
    val xpPoints: Int = 0,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "timer_difficulty")
    val timerDifficulty: TimerDifficulty = TimerDifficulty.Easy,
    @ColumnInfo(name = "daily_tasks_last_refresh_time")
    val dailyTasksLastRefreshTime: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "weekly_tasks_last_refresh_time")
    val weeklyTasksLastRefreshTime: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "monthly_tasks_last_refresh_time")
    val monthlyTasksLastRefreshTime: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "tasks_finished")
    val tasksFinished: Int = 0,
    @ColumnInfo(name = "pages_read")
    val pagesRead: Int = 0,
    @ColumnInfo(name = "available_task_refreshes", defaultValue = "0")
    val availableTaskRefreshes: Int,
    @ColumnInfo(name = "last_timer_rp_gather_time", defaultValue = "0")
    val lastTimerRpGatherTime: Long = 0
)
