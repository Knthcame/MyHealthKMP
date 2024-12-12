package com.knthcame.myhealthkmp

import android.app.Application
import android.content.Context
import org.koin.dsl.bind
import org.koin.dsl.module

class MyHealthApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            module {
                single { applicationContext } bind Context::class
            }
        }
    }
}