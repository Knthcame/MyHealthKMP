package com.knthcame.myhealthkmp

import android.app.Application
import android.content.Context
import com.knthcame.myhealthkmp.data.diary.sources.DiaryDao
import com.knthcame.myhealthkmp.data.diary.sources.buildDiaryDatabase
import com.knthcame.myhealthkmp.data.diary.sources.getDiaryDatabaseBuilder
import org.koin.dsl.bind
import org.koin.dsl.module

class MyHealthApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            val diaryDatabase = buildDiaryDatabase { dbName ->
                getDiaryDatabaseBuilder(applicationContext, dbName)
            }
            module {
                single { applicationContext } bind Context::class
                single<DiaryDao> { diaryDatabase.getDiaryDao() }
            }
        }
    }
}