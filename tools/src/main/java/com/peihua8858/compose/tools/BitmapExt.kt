package com.peihua8858.compose.tools

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ColorMatrixColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint

fun ImageBitmap.toBlackAndWhite(): ImageBitmap {
    val bmpMonochrome = ImageBitmap(width, height)
    val canvas = Canvas(bmpMonochrome)
    val ma = ColorMatrix()
    ma.setToSaturation(0f)
    val paint = Paint()
    paint.colorFilter=(ColorMatrixColorFilter(ma))
    canvas.drawImage(this, Offset(0f, 0f), paint)
    return bmpMonochrome
}
