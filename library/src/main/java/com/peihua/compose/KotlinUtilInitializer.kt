package com.peihua.compose

import android.annotation.SuppressLint
import android.content.Context
import androidx.startup.Initializer
import com.peihua.compose.ContextInitializer.mContext

/**
 * 工具初始化器
 * @author dingpeihua
 * @date 2023/10/12 15:22
 * @version 1.0
 */
class KotlinUtilInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        ContextInitializer.setContext(context.applicationContext)
    }

    override fun dependencies() = emptyList<Class<Initializer<*>>>()


    val context: Context
        get() = ContextInitializer.context
}

@SuppressLint("StaticFieldLeak")
object ContextInitializer {
    private lateinit var mContext: Context

    @JvmStatic
    fun setContext(context: Context) {
        mContext = context
    }

    val context: Context
        get() = mContext
}