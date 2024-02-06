package com.blaubalu.detoxrank.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import com.blaubalu.detoxrank.data.AppDatabase
import com.blaubalu.detoxrank.data.user.OfflineUserDataRepository
import com.blaubalu.detoxrank.ui.utils.Constants.ACTION_SERVICE_CANCEL
import com.blaubalu.detoxrank.ui.utils.Constants.ACTION_SERVICE_START
import com.blaubalu.detoxrank.ui.utils.Constants.NOTIFICATION_CHANNEL_ID
import com.blaubalu.detoxrank.ui.utils.Constants.NOTIFICATION_CHANNEL_NAME
import com.blaubalu.detoxrank.ui.utils.Constants.NOTIFICATION_ID
import com.blaubalu.detoxrank.ui.utils.Constants.TIMER_STATE
import com.blaubalu.detoxrank.ui.utils.formatTime
import com.blaubalu.detoxrank.ui.utils.pad
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/**
 * Handles the main operations for the backend management of all timers in the application
 */
@ExperimentalAnimationApi
@AndroidEntryPoint
class TimerService : Service() {
    @Inject
    lateinit var notificationManager: NotificationManager
    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    private val binder = StopwatchBinder()

    private var duration: Duration = Duration.ZERO
    private lateinit var timer: Timer

    private lateinit var taskTimer: Timer

    private var taskDuration: Duration = Duration.ZERO

    var seconds = mutableStateOf("00")
        private set
    var minutes = mutableStateOf("00")
        private set
    var hours = mutableStateOf("00")
        private set
    var days = mutableStateOf("0")
        private set
    var currentState = mutableStateOf(TimerState.Idle)
        private set

    var secondsDay = mutableStateOf("00")
        private set
    var minutesDay = mutableStateOf("00")
        private set
    var hoursDay = mutableStateOf("00")
        private set

    var daysMonth = mutableStateOf("00")
        private set

    override fun onBind(p0: Intent?) = binder

    /**
     * Handles the actions when the timer is started
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.getStringExtra(TIMER_STATE)) {
            TimerState.Started.name -> {
                startForegroundService()
                startTimer { days, hours, minutes, seconds ->
                    updateNotification(days = days, hours = hours, minutes = minutes, seconds = seconds)
                }
            }
            TimerState.Canceled.name -> {
                cancelTimer()
                stopForegroundService()
            }
        }
        intent?.action.let {
            when (it) {
                ACTION_SERVICE_START -> {
                    startForegroundService()
                    startTimer { days, hours, minutes, seconds ->
                        updateNotification(days = days, hours = hours, minutes = minutes, seconds = seconds)
                    }
                }
                ACTION_SERVICE_CANCEL -> {
                    cancelTimer()
                    stopForegroundService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startTimer(onTick: (d: String, h: String, m: String, s: String) -> Unit) {
        currentState.value = TimerState.Started
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            duration = duration.plus(1.seconds)
            updateTimeUnits()
            onTick(days.value, hours.value, minutes.value, seconds.value)
        }
    }

    /**
     * Updates the timer time shown on screen when the timer section is launched
     */
    suspend fun updateTimerTimeLaunchedEffect(context: Context) {
        val userDataRepository = OfflineUserDataRepository(AppDatabase.getDatabase(context).userDataDao())
        val userData = userDataRepository.getUserStream().first()

        val timerStartTimeMillis = userData.timerStartTimeMillis
        if (userData.timerStarted) {
            val rawTime = System.currentTimeMillis() - timerStartTimeMillis
            duration = rawTime.milliseconds

            if (currentState.value != TimerState.Started) {
                ServiceHelper.triggerForegroundService(
                    context = context,
                    action = ACTION_SERVICE_START
                )
            }
        }
    }

    fun setTaskDuration(time: Duration) {
        taskDuration = time
    }

    fun initTaskTimer() {
        taskTimer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            taskDuration = taskDuration.minus(1.seconds)
            updateTaskDayTimeUnits()
        }
    }

    fun disableTaskTimer() {
        if (this::taskTimer.isInitialized)
            taskTimer.cancel()
    }

    private fun cancelTimer() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        days.value = "0"
        duration = Duration.ZERO
        currentState.value = TimerState.Idle
        updateTimeUnits()
    }

    private fun updateTimeUnits() {
        duration.toComponents { days, hours, minutes, seconds, _ ->
            this@TimerService.days.value = days.toString()
            this@TimerService.hours.value = hours.pad()
            this@TimerService.minutes.value = minutes.pad()
            this@TimerService.seconds.value = seconds.pad()
        }
    }

    private fun updateTaskDayTimeUnits() {
        taskDuration.toComponents { days, hours, minutes, seconds, _ ->
            this@TimerService.daysMonth.value = days.toString()
            this@TimerService.hoursDay.value = hours.toString()
            this@TimerService.minutesDay.value = minutes.pad()
            this@TimerService.secondsDay.value = seconds.pad()
        }
    }

    private fun startForegroundService() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun stopForegroundService() {
        notificationManager.cancel(NOTIFICATION_ID)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            stopForeground(true)
        }
        stopSelf()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun updateNotification(days: String, hours: String, minutes: String, seconds: String) {
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder.setContentText(
                formatTime(
                    days = days,
                    hours = hours,
                    minutes = minutes,
                    seconds = seconds,
                )
            ).build()
        )
    }

    inner class StopwatchBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }
}

enum class TimerState {
    Idle,
    Started,
    Canceled
}
