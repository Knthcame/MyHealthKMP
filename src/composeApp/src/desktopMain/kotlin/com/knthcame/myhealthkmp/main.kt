package com.knthcame.myhealthkmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.knthcame.myhealthkmp.ui.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "MyHealthKMP",
    ) {
        App()
    }
}