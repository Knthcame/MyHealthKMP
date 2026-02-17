package com.knthcame.myhealthkmp.koin

import com.knthcame.myhealthkmp.platformModule
import com.knthcame.myhealthkmp.sharedModule
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.verify.verify

class KoinModulesTest : KoinTest {
    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun verifyDesktopModules() {
        module {
            includes(
                appModule,
                platformModule,
                sharedModule,
            )
        }.verify()
    }
}
