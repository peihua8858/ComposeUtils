package com.peihua8858.compose.tools

import androidx.annotation.DimenRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit


/**
 * Load a dimension resource.
 *
 * @param id the resource identifier
 * @return the dimension value associated with the resource
 */
@Composable
@ReadOnlyComposable
fun dimensionSpResource(@DimenRes id: Int): TextUnit {
    val context = LocalContext.current
    val density = LocalDensity.current
    val pxValue = context.resources.getDimension(id)
    return with(density) { Dp(pxValue / density.density).toSp() }
}