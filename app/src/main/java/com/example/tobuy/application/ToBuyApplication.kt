package com.example.tobuy.application

import android.app.Application
import com.example.tobuy.prefs.Prefs
import dagger.hilt.android.HiltAndroidApp

lateinit var application: ToBuyApplication
    private set

@HiltAndroidApp
class ToBuyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initApplication()
        initPrefs()
    }

    private fun initApplication() {
        application = this
    }

    private fun initPrefs() {
        Prefs.init(application)
    }

}