package com.knthcame.myhealthkmp.ui.common

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

/**
 * Resolves a [Color] based on the [MaterialTheme.colorScheme]'s[ColorScheme.onSurface]
 * and [ColorScheme.inverseOnSurface] based on the luminance of a provided Color.
 *
 * It answers the question: Is this color mostly light or dark?
 * This provides a monochrome contrast color for any provided color. So that's easily legible.
 *
 * For example: A Dark Red will provide [ColorScheme.inverseOnSurface].
 * A light blue will provide [ColorScheme.onSurface]
 *
 * A use case for this would be to provide the textColor of any provided containerColor.
 *
 * @param color The provided color to check for luminance.
 * @return The monochrome contrast color for the provided color.
 */
@Composable
fun resolveMonoChromeFromLuminance(color: Color): Color {
    return if (color.luminance() > 0.6f) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.inverseOnSurface
    }
}