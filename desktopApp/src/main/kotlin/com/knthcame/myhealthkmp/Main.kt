package com.knthcame.myhealthkmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.knthcame.myhealthkmp.ui.App
import org.koin.dsl.module

fun main() =
    application {
        initKoin {
            module {
                // Nothing to be registered here.
            }
        }

        Window(
            onCloseRequest = ::exitApplication,
            title = "MyHealthKMP",
        ) {
            App()
        }
    }