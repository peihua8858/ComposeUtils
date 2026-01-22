package com.peihua8858.compose.tools

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.activity.SystemBarStyle
import androidx.annotation.ColorInt
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.peihua8858.tools.utils.isLandscape

@get:Composable
val isLandscape: Boolean
    get(){
        val context = LocalContext.current
        return context.isLandscape
    }

@get:Composable
val Context.screenWidthDp: Dp
    get() {
        val density = LocalDensity.current
        val widthPixels = resources.displayMetrics.widthPixels
        return (widthPixels.toFloat() / density.density).dp
    }

@get:Composable
val Context.screenHeightDp: Dp
    get() {
        val density = LocalDensity.current
        val heightPixels = resources.displayMetrics.heightPixels
        return (heightPixels.toFloat() / density.density).dp
    }

fun autoSystemBarStyle(
    @ColorInt lightScrim: Color = Color.Transparent,
    @ColorInt darkScrim: Color = Color.Transparent,
    detectDarkMode: (Resources) -> Boolean = { resources -> resources.isSystemDarkMode },
): SystemBarStyle {
    return SystemBarStyle.auto(lightScrim.toArgb(), darkScrim.toArgb(), detectDarkMode)
}

val Context.isSystemDarkMode: Boolean
    get() = resources.isSystemDarkMode

val Resources.isSystemDarkMode: Boolean
    get() = ((configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES)