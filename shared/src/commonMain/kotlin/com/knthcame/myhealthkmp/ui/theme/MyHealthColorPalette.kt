package com.knthcame.myhealthkmp.ui.theme

import androidx.compose.ui.graphics.Color

sealed class MyHealthColorPalette(val activity: Color, val sleep: Color, val warning: Color) {
    data object Light : MyHealthColorPalette(
        activity = activityLight,
        sleep = sleepLight,
        warning = warningLight,
    )

    data object Dark : MyHealthColorPalette(
        activity = activityDark,
        sleep = sleepDark,
        warning = warningDark,
    )
}
