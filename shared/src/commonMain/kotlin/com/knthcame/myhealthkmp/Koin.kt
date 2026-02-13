package com.knthcame.myhealthkmp

import com.knthcame.myhealthkmp.data.dashboard.repositories.DashboardRepository
import com.knthcame.myhealthkmp.data.dashboard.repositories.DefaultDashboardRepository
import com.knthcame.myhealthkmp.data.datetime.repositories.DateTimeRepository
import com.knthcame.myhealthkmp.data.datetime.repositories.DefaultDateTimeRepository
import com.knthcame.myhealthkmp.data.datetime.sources.DateTimeDao
import com.knthcame.myhealthkmp.data.datetime.sources.DefaultDateTimeDao
import com.knthcame.myhealthkmp.data.diary.sources.DiaryDao
import com.knthcame.myhealthkmp.data.diary.sources.FakeDiaryDao
import com.knthcame.myhealthkmp.ui.dashboard.DashboardViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Registers all the dependencies with koin.
 *
 * @param appModule additional module from the app project.
 */
fun initKoin(appModule: () -> Module): KoinApplication =
    startKoin {
        modules(appModule(), platformModule, sharedModule)
    }

private val sharedModule =
    module {
        singleOf(::DefaultDateTimeDao) { bind<DateTimeDao>() }
        singleOf(::DefaultDateTimeRepository) { bind<DateTimeRepository>() }

        singleOf(::FakeDiaryDao) { bind<DiaryDao>() }
        singleOf(::DefaultDashboardRepository) { bind<DashboardRepository>() }

        factory { CoroutineScope(Dispatchers.Main.immediate + SupervisorJob()) }

        viewModelOf(::DashboardViewModel)
    }

/** The module from the platform-specific parts of the shared project */
expect val platformModule: Module
