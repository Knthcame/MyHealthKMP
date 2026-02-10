package com.knthcame.myhealthkmp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.knthcame.myhealthkmp.initKoin
import com.knthcame.myhealthkmp.ui.theme.MyHealthTheme
import org.koin.dsl.module

@Composable
fun App() {
    MyHealthTheme {
        MyHealthNavHost(rememberNavController())
    }
}

@Composable
@Preview
private fun AppPreview() {
    initKoin { module { } }

    App()
}