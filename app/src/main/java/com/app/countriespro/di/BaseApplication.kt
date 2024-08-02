package com.app.countriespro.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication  : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}