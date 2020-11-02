package com.song.sakura.update.manager

import com.song.sakura.update.listener.OnDownloadListener

abstract class BaseHttpDownloadManager {
    /**
     * 下载apk
     *
     * @param apkUrl   apk下载地址
     * @param apkName  apk名字
     * @param listener 回调
     */
    abstract fun download(apkUrl: String, apkName: String, listener: OnDownloadListener)

    /**
     * 取消下载apk
     */
    abstract fun cancel()

    /**
     * 释放资源
     */
    abstract fun release()
}