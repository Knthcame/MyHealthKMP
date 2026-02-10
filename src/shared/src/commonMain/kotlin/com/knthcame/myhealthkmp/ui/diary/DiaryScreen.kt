package com.knthcame.myhealthkmp.ui.diary

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.knthcame.myhealthkmp.ui.common.DefaultScaffold
import myhealthkmp.shared.generated.resources.Res
import myhealthkmp.shared.generated.resources.diary_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun DiaryScreenRoute(onNavigationIconClick: () -> Unit) {
    DiaryScreen(onNavigationIconClick = onNavigationIconClick)
}

@Composable
fun DiaryScreen(onNavigationIconClick: () -> Unit) {
    DefaultScaffold(
        topBarTitle = stringResource(Res.string.diary_title),
        onNavigationIconClick = onNavigationIconClick,
    ) { padding ->
        Text("Diary", modifier = Modifier.padding(padding))
    }
}