package com.song.sakura.extension

import android.content.Context
import com.blankj.utilcode.util.LogUtils

fun Any?.toString(): String {
    if (this == null) return ""
    return toString()
}

fun Any?.logE() {
    if (this == null) return
    LogUtils.e(this)
}

fun Context.getAppVersion(): String {
    return try {
        packageManager.getPackageInfo(packageName, 0).versionName
    } catch (e: Exception) {
        "1.0.0"
    }
}
