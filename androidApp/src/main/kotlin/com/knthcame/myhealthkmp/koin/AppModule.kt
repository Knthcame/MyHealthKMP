package com.knthcame.myhealthkmp.koin

import android.content.Context
import com.knthcame.myhealthkmp.data.diary.sources.DiaryDao
import com.knthcame.myhealthkmp.data.diary.sources.buildDiaryDatabase
import com.knthcame.myhealthkmp.data.diary.sources.getDiaryDatabaseBuilder
import org.koin.dsl.bind
import org.koin.dsl.module

fun appModule(context: Context) =
    module {
        single { context } bind Context::class
        val diaryDatabase =
            buildDiaryDatabase { dbName ->
                getDiaryDatabaseBuilder(context, dbName)
            }
        single<DiaryDao> { diaryDatabase.getDiaryDao() }
    }
