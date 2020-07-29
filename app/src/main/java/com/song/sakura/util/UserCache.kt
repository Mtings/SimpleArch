package com.song.sakura.util

import android.annotation.SuppressLint
import com.ui.util.ACache
import com.ui.util.GsonUtil
import com.song.sakura.app.App

@SuppressLint("Recycle")
object UserCache {

    var username: String? = null

    var authToken: String?
        set(value) {
            try {
                ACache.get(App.getApplication()).put("authToken", value)
            } catch (e: Exception) {
            }
        }
        get() {
            try {
                return ACache.get(App.getApplication()).getAsString("authToken")
            } catch (e: Exception) {
                return ""
            }
        }

}

