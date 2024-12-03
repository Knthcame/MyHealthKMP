package com.knthcame.myhealthkmp.ui.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import myhealthkmp.composeapp.generated.resources.Res
import myhealthkmp.composeapp.generated.resources.dashboard_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun DashboardScreenRoute() {
    DashboardScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(Res.string.dashboard_title))
                },
            )
        },
    ) { padding ->
        Text("Dashboard", modifier = Modifier.padding(padding))
    }
}