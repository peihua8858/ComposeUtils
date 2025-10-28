package com.peihua.compose.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.core.graphics.createBitmap
import androidx.exifinterface.media.ExifInterface

fun Bitmap.toBlackAndWhite(): Bitmap {
    val bmpMonochrome = createBitmap(width, height)
    val canvas = Canvas(bmpMonochrome)
    val ma = ColorMatrix()
    ma.setSaturation(0f)
    val paint = Paint()
    paint.setColorFilter(ColorMatrixColorFilter(ma))
    canvas.drawBitmap(this, 0f, 0f, paint)
    return bmpMonochrome
}

fun ImageBitmap.toBlackAndWhite(): ImageBitmap {
    val bmpMonochrome = ImageBitmap(width, height)
    val canvas = androidx.compose.ui.graphics.Canvas(bmpMonochrome)
    val ma = androidx.compose.ui.graphics.ColorMatrix()
    ma.setToSaturation(0f)
    val paint = androidx.compose.ui.graphics.Paint()
    paint.colorFilter=(androidx.compose.ui.graphics.ColorMatrixColorFilter(ma))
    canvas.drawImage(this, Offset(0f, 0f), paint)
    return bmpMonochrome
}

fun Bitmap.transformBitmap(transformMatrix: Matrix): Bitmap {
    try {
        val converted = Bitmap.createBitmap(
            this,
            0,
            0,
            getWidth(),
            getHeight(),
            transformMatrix,
            true
        )
        if (!sameAs(converted)) {
           return converted
        }
    } catch (error: OutOfMemoryError) {
        dLog { "transformBitmap: $error" }
    }
    return this
}


/**
 * Bitmap高斯模糊
 * @param   context 当前上下文
 * @param   bitmap 要模糊的图片
 * @param   radius 模糊半径
 * @param   sale 图片缩放比例
 * @author dingpeihua
 * @date 2022/5/13 16:54
 * @version 1.0
 */
fun Bitmap.blur(context: Context, radius:Float, sale:Float): Bitmap {
    //创建一个缩小后的bitmap
    val inputBitmap = Bitmap.createScaledBitmap(this, (width/sale).toInt(),
        (height/sale).toInt(), false)
    //创建将在ondraw中使用到的经过模糊处理后的bitmap
    val outputBitmap = Bitmap.createBitmap(inputBitmap)

    //创建RenderScript，ScriptIntrinsicBlur固定写法
    val rs: RenderScript = RenderScript.create(context)
    val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

    //根据inputBitmap，outputBitmap分别分配内存
    val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
    val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)

    //设置模糊半径取值0-25之间，不同半径得到的模糊效果不同
    blurScript.setRadius(radius)
    blurScript.setInput(tmpIn)
    blurScript.forEach(tmpOut)

    //得到最终的模糊bitmap
    tmpOut.copyTo(outputBitmap)
    return outputBitmap
}


fun Bitmap.adjustBitmapOrientation(filePath: String): Bitmap? {
    var exifInterface: ExifInterface? = null
    try {
        exifInterface = ExifInterface(filePath)
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    var rotation = 0
    if (exifInterface != null) {
        val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotation = 90
            ExifInterface.ORIENTATION_ROTATE_180 -> rotation = 180
            ExifInterface.ORIENTATION_ROTATE_270 -> rotation = 270
            else -> {}
        }
    }
    dLog { "adjustBitmapOrientation, adjust degree " + rotation + "to 0." }
    if (rotation == 0) {
        return this
    }
    val matrix = Matrix()
    matrix.postRotate(rotation.toFloat())
    return Bitmap.createBitmap(
        this,
        0,
        0,
        getWidth(),
        getHeight(),
        matrix,
        true
    )
}
