package com.knthcame.myhealthkmp.koin

import android.content.Context
import org.koin.dsl.bind
import org.koin.dsl.module

fun appModule(context: Context) = module {
    single { context } bind Context::class
}