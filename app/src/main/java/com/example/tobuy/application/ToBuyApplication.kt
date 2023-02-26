package com.example.tobuy.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

lateinit var application: ToBuyApplication
    private set

@HiltAndroidApp
class ToBuyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initApplication()
    }

    private fun initApplication() {
        application = this
    }
}