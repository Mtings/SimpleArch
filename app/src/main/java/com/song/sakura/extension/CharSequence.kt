package com.song.sakura.extension

import com.hjq.toast.ToastUtils

/**
 * 弹出Toast提示。
 */
fun CharSequence.showToast() {
    ToastUtils.show(this)
}