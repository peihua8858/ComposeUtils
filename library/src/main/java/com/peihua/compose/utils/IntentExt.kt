package com.peihua.compose.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable

fun <T> Intent?.getParcelableExtraCompat(name: String?, clazz: Class<T>): T? {
    if (this == null) return null
    if (isTiramisu) {
        return getParcelableExtra(name, clazz)
    } else {
        return getParcelableExtra(name)
    }
}

fun <T : Parcelable> Intent?.getParcelableArrayListExtraCompat(name: String?, clazz: Class<T>): ArrayList<T> {
    if (this == null) return arrayListOf()
    if (isTiramisu) {
        return getParcelableArrayListExtra(name, clazz) ?: arrayListOf()
    } else {
        return getParcelableArrayListExtra(name) ?: arrayListOf()
    }
}

fun <T> Bundle?.getParcelableCompat(name: String?, clazz: Class<T>): T? {
    if (this == null) return null
    if (isTiramisu) {
        return getParcelable(name, clazz)
    } else {
        return getParcelable(name)
    }
}

fun <T : Parcelable> Bundle?.getParcelableArrayListCompat(name: String?, clazz: Class<T>): ArrayList<T> {
    if (this == null) return arrayListOf()
    if (isTiramisu) {
        return getParcelableArrayList(name, clazz) ?: arrayListOf()
    } else {
        return getParcelableArrayList(name) ?: arrayListOf()
    }
}


fun Context.registerReceiverExported(receiver: BroadcastReceiver, intent: IntentFilter) {
    if (isTiramisu) {
        registerReceiver(receiver, intent, Context.RECEIVER_EXPORTED)
    } else registerReceiver(receiver, intent)
}

fun Context.registerReceiverNotExported(receiver: BroadcastReceiver, intent: IntentFilter) {
    if (isTiramisu) {
        registerReceiver(receiver, intent, Context.RECEIVER_NOT_EXPORTED)
    } else registerReceiver(receiver, intent)
}