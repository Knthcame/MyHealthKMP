package com.knthcame.myhealthkmp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.knthcame.myhealthkmp.ui.theme.MyHealthTheme

@Composable
@Preview
fun App() {
    MyHealthTheme {
        MyHealthNavHost(rememberNavController())
    }
}