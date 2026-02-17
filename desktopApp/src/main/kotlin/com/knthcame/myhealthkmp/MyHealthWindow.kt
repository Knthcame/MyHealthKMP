package com.knthcame.myhealthkmp

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import com.knthcame.myhealthkmp.ui.App

@Composable
fun MyHealthWindow(onCloseRequest: () -> Unit) {

    Window(
        onCloseRequest = onCloseRequest,
        title = "MyHealthKMP",
    ) {
        App()
    }
}