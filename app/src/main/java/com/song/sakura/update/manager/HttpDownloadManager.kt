package com.song.sakura.update.manager

import com.blankj.utilcode.util.LogUtils
import com.song.sakura.update.listener.OnDownloadListener
import com.song.sakura.util.FileUtil
import kotlinx.coroutines.*
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

class HttpDownloadManager(private val downloadPath: String) : BaseHttpDownloadManager() {

    companion object {
        private const val TAG = "UpdateApp: HttpDownloadManager"
        private const val HTTP_TIME_OUT = 30000
    }

    private var shutdown = false
    private var apkUrl: String = ""
    private var apkName: String = ""
    private var listener: OnDownloadListener? = null

    override fun download(apkUrl: String, apkName: String, listener: OnDownloadListener) {
        this.apkUrl = apkUrl
        this.apkName = apkName
        this.listener = listener
        CoroutineScope(Dispatchers.Main)
            .launch {
                withContext(Dispatchers.IO) {
                    if (FileUtil.fileExists(downloadPath, apkName)) {
                        FileUtil.delete(downloadPath, apkName)
                    }
                    fullDownload()
                }
            }
    }

    override fun cancel() {
        shutdown = true
    }

    override fun release() {
        listener = null
    }

    /**
     * 全部下载
     */
    private fun fullDownload() {
        listener?.start()
        try {
            val url = URL(apkUrl)
            val con = url.openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.readTimeout = HTTP_TIME_OUT
            con.connectTimeout = HTTP_TIME_OUT
            con.setRequestProperty("Accept-Encoding", "identity")
            if (con.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = con.inputStream
                val length = con.contentLength
                var len: Int
                //当前已下载完成的进度
                var progress = 0
                val buffer = ByteArray(1024 * 2)
                val file = FileUtil.createFile(downloadPath, apkName)
                val stream = FileOutputStream(file)
                while (inputStream.read(buffer).also { len = it } != -1 && !shutdown) {
                    //将获取到的流写入文件中
                    stream.write(buffer, 0, len)
                    progress += len
                    listener?.downloading(length, progress)
                }
                if (shutdown) {
                    //取消了下载 同时再恢复状态
                    shutdown = false
                    LogUtils.eTag(TAG, "fullDownload: 取消了下载")
                    listener?.cancel()
                } else {
                    listener?.done(file)
                }
                //完成io操作,释放资源
                stream.flush()
                stream.close()
                inputStream.close()
                //重定向
            } else if (con.responseCode == HttpURLConnection.HTTP_MOVED_PERM
                || con.responseCode == HttpURLConnection.HTTP_MOVED_TEMP
            ) {
                apkUrl = con.getHeaderField("Location")
                con.disconnect()
                LogUtils.eTag(TAG, "fullDownload: 当前地址是重定向Url，定向后的地址：$apkUrl")
                fullDownload()
            } else {
                listener?.error(SocketTimeoutException("下载失败：Http ResponseCode = " + con.responseCode))
            }
            con.disconnect()
        } catch (e: Exception) {
            listener?.error(e)
            e.printStackTrace()
        }
    }
}