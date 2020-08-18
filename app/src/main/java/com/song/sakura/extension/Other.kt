package com.song.sakura.extension

import android.content.Context

fun Any?.toString(): String {
    if (this == null) return ""
    return toString()
}

fun Context.getAppVersion(): String {
    return try {
        packageManager.getPackageInfo(packageName, 0).versionName
    } catch (e: Exception) {
        "1.0.0"
    }
}
