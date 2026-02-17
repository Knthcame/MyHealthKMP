package com.knthcame.myhealthkmp

import android.app.Application
import com.knthcame.myhealthkmp.koin.appModule

class MyHealthApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            appModule(applicationContext)
        }
    }
}