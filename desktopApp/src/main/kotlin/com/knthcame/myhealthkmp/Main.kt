package com.knthcame.myhealthkmp

import androidx.compose.ui.window.application

fun main() =
    application {

        initKoin { appModule }

        MyHealthWindow(onCloseRequest = ::exitApplication)
    }
