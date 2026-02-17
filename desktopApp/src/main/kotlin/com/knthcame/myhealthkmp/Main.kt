package com.knthcame.myhealthkmp

import androidx.compose.ui.window.application
import com.knthcame.myhealthkmp.koin.appModule

fun main() =
    application {
        initKoin { appModule }

        MyHealthWindow(onCloseRequest = ::exitApplication)
    }
