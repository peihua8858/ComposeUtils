@file:JvmName("NumberUtil")
@file:JvmMultifileClass

package com.peihua.compose.utils

inline fun Double?.ifNullOrEmpty(defaultValue: () -> Double): Double =
    if (this == null || isNaN()) defaultValue() else this

inline fun Int?.ifNullOrEmpty(defaultValue: () -> Int): Int =
    this ?: defaultValue()

inline fun Float?.ifNullOrEmpty(defaultValue: () -> Float): Float =
    if (this == null || isNaN()) defaultValue() else this

inline fun Long?.ifNullOrEmpty(defaultValue: () -> Long): Long =
    this ?: defaultValue()
