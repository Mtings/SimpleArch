package com.song.sakura.extension

import com.hjq.toast.ToastUtils

/**
 * 弹出Toast提示。
 */
fun CharSequence.showToast() {
    ToastUtils.show(this)
}

fun CharSequence?.isBlank(): Boolean {
    val strLen: Int = this?.length ?: 0
    if (this == null || strLen == 0) {
        return true
    }
    for (i in 0 until strLen) {
        if (!Character.isWhitespace(this.elementAt(i))) {
            return false
        }
    }
    return true
}