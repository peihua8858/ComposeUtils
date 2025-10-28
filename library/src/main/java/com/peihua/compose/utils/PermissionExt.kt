package com.peihua.compose.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.provider.Settings
import androidx.core.content.ContextCompat
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


fun Context.checkPermissions(vararg permission: String): Boolean {
    if (permission.isEmpty()) {
        return true
    }
    for (p in permission) {
        if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }
    return true
}

@OptIn(ExperimentalStdlibApi::class)
fun Context.isGrantedPermission(permission: String): Boolean {
    if (permission.isEmpty() || !isAtLeastM) {
        return true
    }
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

@OptIn(ExperimentalContracts::class)
fun Context.isGrantedPermission(vararg permissions: String): Boolean {
    contract { returns() }
    if (permissions.isEmpty() || !isAtLeastM) {
        return true
    }
    return permissions.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }
}


@OptIn(ExperimentalContracts::class)
fun Context.isGrantedStoragePermission(): Boolean {
    if (isAtLeastR) {
        return Environment.isExternalStorageManager()
    }
    return isGrantedPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
}

fun isGrantedWindowPermission(context: Context?): Boolean {
    if (isAtLeastM) {
        return Settings.canDrawOverlays(context)
    }
    return true
}