package com.knthcame.myhealthkmp

import android.app.Application

class MyHealthApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            appModule(applicationContext)
        }
    }
}