package com.blaubalu.detoxrank.daggerhilt

import android.app.NotificationManager
import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.app.NotificationCompat
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.service.ServiceHelper
import com.blaubalu.detoxrank.ui.utils.Constants.NOTIFICATION_CHANNEL_ID
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped


/**
 * Notification Manager and Notification Builder setup
 */
@ExperimentalAnimationApi
@Module
@InstallIn(ServiceComponent::class)
object NotificationModule {
    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Detox Active!")
            .setContentText("Day 0, 00:00:00")
            .setSmallIcon(R.drawable.timernavicon)
            .setOngoing(true)
            .setContentIntent(ServiceHelper.clickPendingIntent(context))
    }

    @ServiceScoped
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}