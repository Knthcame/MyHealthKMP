package com.knthcame.myhealthkmp

import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.module.Module
import org.koin.dsl.module

/** The module from the platform-specific parts of the shared project */
actual val platformModule: Module =
    module {
        // Nothing here yet
    }

/**
 * Registers the provided classes in the koin dependency injection manager as part of the
 * application module, alongside the [sharedModule] and [platformModule].
 */
@Suppress("unused") // Called from Swift
fun initKoinIos(): KoinApplication =
    initKoin {
        module {
            // Nothing here yet.
        }
    }

/** Exposes container resolved instances to swift */
@Suppress("unused") // Called from Swift
object KotlinDependencies : KoinComponent
