package com.song.sakura.util

import android.app.Application
import android.content.Context
import androidx.core.content.ContextCompat
import com.ui.util.DrawableHelper
import com.ui.util.Utils
import com.song.sakura.app.App

fun Any?.toString(): String {
    if (this == null) return ""
    return toString()
}

fun Application.dip2px(dp: Float) = Utils.dip2px(this.applicationContext, dp)

fun dip2px(dp: Float) = Utils.dip2px(App.getApplication(), dp)

fun Int.dip2px() = Utils.dip2px(App.getApplication(), toFloat())

fun Float.dp2px() = Utils.dip2px(App.getApplication(), toFloat())

fun Int.toColor() = ContextCompat.getColor(App.getApplication(), toInt())

fun getDrawable(resId: Int) = DrawableHelper.getDrawable(App.getApplication(), resId)

fun getVersion(context: Context): String {
    var appVersion: String
    try {
        appVersion = context.packageManager.getPackageInfo(context.packageName, 0).versionName
    } catch (e: Exception) {
        appVersion = "1.0.0"
    }

    return appVersion
}
