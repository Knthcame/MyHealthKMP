package com.knthcame.myhealthkmp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.knthcame.myhealthkmp.ui.MyHealthNavHost
import com.knthcame.myhealthkmp.ui.theme.MyHealthTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MyHealthTheme {
        MyHealthNavHost(rememberNavController())
    }
}