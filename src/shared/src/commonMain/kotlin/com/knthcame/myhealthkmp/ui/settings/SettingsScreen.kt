package com.knthcame.myhealthkmp.ui.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.knthcame.myhealthkmp.ui.common.DefaultScaffold
import myhealthkmp.shared.generated.resources.Res
import myhealthkmp.shared.generated.resources.settings_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsScreenRoute(onNavigationIconClick: () -> Unit) {
    SettingsScreen(onNavigationIconClick = onNavigationIconClick)
}

@Composable
fun SettingsScreen(onNavigationIconClick: () -> Unit) {
    DefaultScaffold(
        topBarTitle = stringResource(Res.string.settings_title),
        onNavigationIconClick = onNavigationIconClick,
    ) { padding ->
        Text("Settings", modifier = Modifier.padding(padding))
    }
}
