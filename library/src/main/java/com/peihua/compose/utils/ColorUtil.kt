@file:JvmName("ColorUtil")
@file:JvmMultifileClass

package com.peihua.compose.utils

import android.content.Context
import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.compose.ui.graphics.toArgb
import kotlin.math.roundToInt

/**
 * 十六进制正则表达式
 * 匹配#ccc或者#cccccc或者#cccccccc
 */
private const val HEX_PATTERN = "(^#[0-9A-Fa-f]{3}\$)|(^#[0-9A-Fa-f]{6}\$)|(^#[0-9A-Fa-f]{8}\$)"

/**
 * 颜色值最小长度
 */
private const val HEX_MIN_LENGTH = 4

/**
 * 如：#cccccc
 */
private const val HEX_MID_LENGTH = 7

/**
 * 颜色值最大长度
 */
private const val HEX_MAX_LENGTH = 9

/**
 * 解析#ccc、#cccccc、#cccccccc三种形式
 */
@ColorInt
fun CharSequence?.parseColor(@ColorInt defaultColor: Int = android.graphics.Color.BLACK): Int {
    this ?: return defaultColor
    if (isHexColor()) {
        var color = this.toString()
        if (length == HEX_MIN_LENGTH) {
            color = color.toHexColor()
            if (color.isEmpty()) return defaultColor
        }
        return color.toColorInt()
    }
    return defaultColor
}

fun String.toHexColor(): String {
    if (isEmpty()) return ""
    var firstChar = this[0]
    val isHashtag = firstChar == '#'
    if (!isHashtag) {
        firstChar = '#'
    }
    val color = if (isHashtag) subSequence(1, length) else this
    val result = arrayListOf<String>()
    for (c in color) {
        result.add(c.toString().repeat(2))
    }
    return firstChar + result.joinToString("")
}

/**
 * 判断是否是十六进制颜色值
 *
 * @param color
 * @author dingpeihua
 * @date 2019/12/12 15:46
 * @version 1.0
 */
fun CharSequence?.isHexColor(): Boolean {
    if (isNullOrEmpty()) {
        return false
    }
    val len = length
    return ((len == HEX_MIN_LENGTH || len == HEX_MAX_LENGTH || len == HEX_MID_LENGTH)
            && this.matches(Regex(HEX_PATTERN)))
}

/**
 * 将十六进制颜色值转换成颜色数值，转换失败默认返回[android.graphics.Color.TRANSPARENT]
 *
 * @param color 十六进制颜色值
 * @author dingpeihua
 * @date 2019/9/19 17:21
 * @version 1.0
 */
@ColorInt
fun String?.getColorInt(): Int {
    return getColorInt(android.graphics.Color.TRANSPARENT)
}

/**
 * 将十六进制颜色值转换成颜色数值，转换失败返回默认值
 *
 * @param color        十六进制颜色值
 * @param defaultColor 默认颜色值
 * @author dingpeihua
 * @date 2019/9/19 17:21
 * @version 1.0
 */
@ColorInt
fun String?.getColorInt(@ColorInt defaultColor: Int): Int {
    return parseColor(defaultColor)
}

/**
 * 将颜色字符串变换成颜色状态列表
 *
 * @param normalColor
 * @param pressedColor
 * @author dingpeihua
 * @date 2019/9/19 16:52
 * @version 1.0
 */
fun Any?.getColorStateList(normalColor: String, pressedColor: String): ColorStateList {
    val normalColorInt = normalColor.getColorInt()
    val pressedColorInt = pressedColor.getColorInt()
    return if (normalColorInt != -1 && pressedColorInt != -1) {
        getColorStateList(normalColorInt, pressedColorInt)
    } else ColorStateList(arrayOf(), intArrayOf(android.graphics.Color.WHITE))
}

fun Any?.getColorStateList(
    context: Context?,
    @ColorRes normalColorRes: Int,
    @ColorRes pressedColorRes: Int,
): ColorStateList {
    return getColorStateList(
        ContextCompat.getColor(context!!, normalColorRes),
        ContextCompat.getColor(context, pressedColorRes)
    )
}

fun Any?.getColorStateList(@ColorInt normalColor: Int, @ColorInt selectedColor: Int): ColorStateList {
    val states = arrayOf(
        intArrayOf(android.R.attr.state_enabled),
        intArrayOf(android.R.attr.state_selected),
        intArrayOf(-android.R.attr.state_selected),
        intArrayOf(android.R.attr.state_checked),
        intArrayOf(-android.R.attr.state_checked),
        intArrayOf(android.R.attr.state_pressed),
        intArrayOf(-android.R.attr.state_pressed)
    )
    val colors = intArrayOf(
        normalColor,
        selectedColor,
        normalColor,
        selectedColor,
        normalColor,
        selectedColor,
        normalColor
    )
    return ColorStateList(states, colors)
}

fun Any?.getColorStateList(
    @ColorInt normalColor: Int,
    disabledColor: Int,
    @ColorInt selectedColor: Int,
): ColorStateList {
    val states = arrayOf(
        intArrayOf(android.R.attr.state_enabled),
        intArrayOf(-android.R.attr.state_enabled),
        intArrayOf(android.R.attr.state_selected),
        intArrayOf(-android.R.attr.state_selected),
        intArrayOf(android.R.attr.state_checked),
        intArrayOf(-android.R.attr.state_checked),
        intArrayOf(android.R.attr.state_pressed),
        intArrayOf(-android.R.attr.state_pressed)
    )
    val colors = intArrayOf(
        normalColor,
        disabledColor,
        selectedColor,
        normalColor,
        selectedColor,
        normalColor,
        selectedColor,
        normalColor
    )
    return ColorStateList(states, colors)
}


/**
 * Returns an integer array for all color channels value.
 */
fun Color.argb(): Array<Int> {
    val argb = toArgb()
    val alpha = argb shr 24 and 0xff
    val red = argb shr 16 and 0xff
    val green = argb shr 8 and 0xff
    val blue = argb and 0xff
    return arrayOf(alpha, red, green, blue)
}

/**
 * Returns the red value as an integer.
 */
fun Color.red(): Int {
    return toArgb() shr 16 and 0xff
}

/**
 * Returns the green value as an integer.
 */
fun Color.green(): Int {
    return toArgb() shr 8 and 0xff
}

/**
 * Returns the blue value as an integer.
 */
fun Color.blue(): Int {
    return toArgb() and 0xff
}

/**
 * Returns the alpha value as an integer.
 */
fun Color.alpha(): Int {
    return toArgb() shr 24 and 0xff
}

/**
 * Returns ARGB color as a hex string.
 * @param hexPrefix Add # char before the hex number.
 * @param includeAlpha Include the alpha value within the hex string.
 */
fun Color.toHex(hexPrefix: Boolean = false, includeAlpha: Boolean = true): String {
    val (alpha, red, green, blue) = argb()
    return buildString {
        if (hexPrefix) {
            append("#")
        }
        if (includeAlpha) {
            append(alpha.toHex())
        }
        append(red.toHex())
        append(green.toHex())
        append(blue.toHex())
    }
}

private fun Int.toHex(): String {
    return Integer.toHexString(this).let {
        if (it.length == 1) {
            "0$it"
        } else {
            it
        }
    }
}

internal fun Double.lighten(lightness: Float): Double {
    return this + (255 - this) * lightness
}

internal fun Float.lighten(lightness: Float): Float {
    return this + (255 - this) * lightness
}

internal fun Int.lighten(lightness: Float): Int {
    return (this + (255 - this) * lightness).roundToInt()
}

internal fun Double.darken(darkness: Float): Double {
    return this - this * darkness
}

internal fun Float.darken(darkness: Float): Float {
    return this - this * darkness
}

internal fun Int.darken(darkness: Float): Int {
    return (this - this * darkness).roundToInt()
}