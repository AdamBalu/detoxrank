package com.blaubalu.detoxrank

import android.app.Application
import com.blaubalu.detoxrank.data.AppContainer
import com.blaubalu.detoxrank.data.AppDataContainer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DetoxRankApp: Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
