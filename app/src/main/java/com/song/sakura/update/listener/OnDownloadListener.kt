package com.song.sakura.update.listener

import java.io.File

public interface OnDownloadListener {

    /**
     * 开始下载
     */
    fun start()

    /**
     * 下载中
     *
     * @param max      总进度
     * @param progress 当前进度
     */
    fun downloading(max: Int, progress: Int)

    /**
     * 下载完成
     *
     * @param apk 下载好的apk
     */
    fun done(apk: File?)

    /**
     * 取消下载
     */
    fun cancel()

    /**
     * 下载出错
     *
     * @param e 错误信息
     */
    fun error(e: Exception?)

}