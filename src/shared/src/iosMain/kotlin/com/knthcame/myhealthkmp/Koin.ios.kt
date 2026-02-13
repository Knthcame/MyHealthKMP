package com.knthcame.myhealthkmp

import com.knthcame.myhealthkmp.data.diary.sources.DiaryDao
import com.knthcame.myhealthkmp.data.diary.sources.buildDiaryDatabase
import com.knthcame.myhealthkmp.data.diary.sources.getDiaryDatabaseBuilder
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.module.Module
import org.koin.dsl.module

/** The module from the platform-specific parts of the shared project */
actual val platformModule: Module = module {
    val diaryDatabase = buildDiaryDatabase { dbName ->
        getDiaryDatabaseBuilder(dbName)
    }

    single<DiaryDao> { diaryDatabase.getDiaryDao() }
}

/**
 * Registers the provided classes in the koin dependency injection manager as part of the
 * application module, alongside the [sharedModule] and [platformModule].
 */
@Suppress("unused") // Called from Swift
fun initKoinIos(): KoinApplication = initKoin {
    module {
        // Nothing here yet.
    }
}

@Suppress("unused") // Called from Swift
/** Exposes container resolved instances to swift */
object KotlinDependencies : KoinComponent