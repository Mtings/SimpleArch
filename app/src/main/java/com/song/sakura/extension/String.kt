package com.song.sakura.extension

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.song.sakura.BuildConfig

fun String.logE() {
    if (BuildConfig.DEBUG) {
        Log.e("Song: ", this)
    }
}

fun String.copy(context: Context) {
    val cm =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val data = ClipData.newPlainText("", this)
    cm.setPrimaryClip(data)
    Toast.makeText(context.applicationContext, "复制成功", Toast.LENGTH_SHORT)
        .show()
}

fun String.openBrowser(context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(this))
    context.startActivity(intent)
}

fun String.checkAppInstalled(context: Context): Boolean {
    if (TextUtils.isEmpty(this)) {
        return false
    }
    val packageManager: PackageManager = context.packageManager
    val infoList = packageManager.getInstalledPackages(0)
    if (infoList.isNullOrEmpty()) {
        return false
    }
    for (packageInfo in infoList) {
        if (this == packageInfo.packageName) {
            return true
        }
    }
    return false
}