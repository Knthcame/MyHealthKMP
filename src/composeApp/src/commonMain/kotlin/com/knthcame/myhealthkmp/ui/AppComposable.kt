package com.knthcame.myhealthkmp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.knthcame.myhealthkmp.ui.theme.MyHealthTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Composable
@Preview
fun App() {
    KoinContext {
        MyHealthTheme {
            MyHealthNavHost(rememberNavController())
        }
    }
}