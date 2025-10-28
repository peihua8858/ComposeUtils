package com.peihua.compose.network

import android.content.Context
import com.peihua.compose.ContextInitializer

fun Context.isConnected(): Boolean {
    return Connectivity.isConnected(this)
}

fun Context.getNetworkType(): NetworkType {
    return NetworkUtil.getNetworkType(this)
}

fun Context.isConnectedWifi(context: Context): Boolean {
    return NetworkUtil.isConnectedWifi(context)
}

fun Context.isConnectedMobile(context: Context): Boolean {
    return NetworkUtil.isConnectedMobile(context)
}

fun Context.isConnectionFast(context: Context): Boolean {
    return NetworkUtil.isConnectionFast(context)
}

/**
 * 网络工具类
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2017/11/8 10:18
 */
object NetworkUtil {

    /**
     * 检测网络是否连接
     *
     * @author dingpeihua
     * @date 2016/10/19 16:42
     * @version 1.0
     */
    @JvmStatic
    fun isConnected(): Boolean {
        return ContextInitializer.context.let { Connectivity.isConnected(it) }
    }

    /**
     * 检测网络是否连接
     *
     * @author dingpeihua
     * @date 2016/10/19 16:42
     * @version 1.0
     */
    @JvmStatic
    fun isConnected(context: Context): Boolean {
        return Connectivity.isConnected(context)
    }

    @JvmStatic
    fun getNetworkType(context: Context): NetworkType {
        return Connectivity.getNetworkType(context)
    }

    @JvmStatic
    fun isConnectedWifi(context: Context): Boolean {
        return Connectivity.isConnectedWifi(context)
    }

    @JvmStatic
    fun isConnectedMobile(context: Context): Boolean {
        return Connectivity.isConnectedMobile(context)
    }

    @JvmStatic
    fun isConnectionFast(context: Context): Boolean {
        return Connectivity.isConnectionFast(context)
    }
}