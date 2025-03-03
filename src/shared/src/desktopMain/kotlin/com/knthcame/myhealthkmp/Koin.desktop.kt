package com.knthcame.myhealthkmp

import com.knthcame.myhealthkmp.data.diary.sources.DiaryDao
import com.knthcame.myhealthkmp.data.diary.sources.buildDiaryDatabase
import com.knthcame.myhealthkmp.data.diary.sources.getDiaryDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

/** The module from the platform-specific parts of the shared project */
actual val platformModule: Module = module {
    val diaryDatabase = buildDiaryDatabase { dbName ->
        getDiaryDatabaseBuilder(dbName)
    }
    single<DiaryDao> { diaryDatabase.getDiaryDao() }
}