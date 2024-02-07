package com.blaubalu.detoxrank.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.content.ContextCompat
import com.blaubalu.detoxrank.MainActivity
import com.blaubalu.detoxrank.ui.utils.Constants.CANCEL_REQUEST_CODE
import com.blaubalu.detoxrank.ui.utils.Constants.CLICK_REQUEST_CODE
import com.blaubalu.detoxrank.ui.utils.Constants.TIMER_STATE

/**
 * Helper object for timer service
 */
@ExperimentalAnimationApi
object ServiceHelper {

    private val flag =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            PendingIntent.FLAG_IMMUTABLE
        else
            0

    /**
     * Click pending intent for timer service
     */
    fun clickPendingIntent(context: Context): PendingIntent {
        val clickIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(TIMER_STATE, TimerState.Started.name)
        }
        return PendingIntent.getActivity(
            context, CLICK_REQUEST_CODE, clickIntent, flag
        )
    }

    /**
     * Cancel pending intent for timer service
     */
    fun cancelPendingIntent(context: Context): PendingIntent {
        val cancelIntent = Intent(context, TimerService::class.java).apply {
            putExtra(TIMER_STATE, TimerState.Canceled.name)
        }
        return PendingIntent.getService(
            context, CANCEL_REQUEST_CODE, cancelIntent, flag
        )
    }

    fun triggerForegroundService(context: Context, action: String): Boolean {
        if (!getNeededPermissions(context)) return false
        Intent(context, TimerService::class.java).apply {
            this.action = action
            context.startService(this)
        }
        return true
    }

    /**
     * Handles needed user permissions
     * @return true if the permissions are set correctly in advance
     */
    private fun getNeededPermissions(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager =
                ContextCompat.getSystemService(context, AlarmManager::class.java)
            if (alarmManager?.canScheduleExactAlarms() == false) {
                Intent().also { intent ->
                    intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                    context.startActivity(intent)
                }
                return false
            }
        }
        return true
    }
}