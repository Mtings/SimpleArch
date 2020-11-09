package com.song.sakura.update

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.song.sakura.R
import com.song.sakura.aop.SingleClick
import com.song.sakura.update.listener.OnDownloadListener
import com.song.sakura.update.manager.DownloadManager
import com.song.sakura.update.service.DownloadService
import com.song.sakura.util.ApkUtil
import com.ui.base.BaseDialog
import java.io.File

class UpdateDialog {
    class Builder(context: Context) : BaseDialog.Builder<Builder>(context), OnDownloadListener {

        private val mNameView: TextView
        private val mContentView: TextView
        private val mProgressView: ProgressBar
        private val mUpdateView: TextView
        private val mCloseView: TextView
        private val apkSize: TextView

        private var AUTHORITIES: String = ""

        init {
            setContentView(R.layout.dialog_update)
            setAnimStyle(BaseDialog.BOTTOM)
            setCancelable(false)
            mNameView = findViewById(R.id.versionName)
            mContentView = findViewById(R.id.updateContent)
            mProgressView = findViewById(R.id.progress)
            mUpdateView = findViewById(R.id.btnUpdate)
            mCloseView = findViewById(R.id.btnClose)
            apkSize = findViewById(R.id.apkSize)

            AUTHORITIES = DownloadManager.manager?.getAuthorities() ?: ""
            DownloadManager.manager?.setOnDownloadListener(this)

            setOnClickListener(mUpdateView, mCloseView)
        }

        /*** Apk 文件 */
        private var mApkFile: File? = null

        /*** 是否强制更新 */
        private var mForceUpdate = false

        /*** 当前是否下载中 */
        private var mDownloading = false

        /*** 当前是否下载完毕 */
        private var mDownloadComplete = false

        /*** 设置版本名 */
        fun setVersionName(name: CharSequence): Builder {
            mNameView.text = name
            return this
        }

        fun setApkSize(size: String): Builder {
            apkSize.text = "约${size}"
            apkSize.visibility = if (TextUtils.isEmpty(size)) View.GONE else View.VISIBLE
            return this
        }

        /*** 设置更新日志 */
        fun setUpdateLog(text: CharSequence): Builder {
            mContentView.text = text
            mContentView.visibility = if (TextUtils.isEmpty(text)) View.GONE else View.VISIBLE
            return this
        }

        /*** 设置强制更新 */
        fun setForceUpdate(force: Boolean): Builder {
            mForceUpdate = force
            mCloseView.visibility = if (force) View.GONE else View.VISIBLE
//            setCancelable(!force)
            return this
        }

        @SingleClick
        override fun onClick(v: View) {
            if (v === mCloseView) {
                dismiss()
            } else if (v === mUpdateView) {
                // 判断下载状态
                if (mDownloadComplete) {
                    if (mApkFile != null && mApkFile!!.isFile) {
                        // 下载完毕，安装 Apk
                        ApkUtil.installApk(context, AUTHORITIES, mApkFile)
                        dismiss()
                    } else {
                        // 下载失败，重新下载
                        context.startService(Intent(context, DownloadService::class.java))
                    }
                } else if (!mDownloading) {
                    // 没有下载，开启下载
                    context.startService(Intent(context, DownloadService::class.java))
                }
            }
        }

        override fun start() {
            // 标记成未下载完成
            mDownloadComplete = false
            // 显示进度条
            mProgressView.progress = 0
            mProgressView.visibility = View.VISIBLE
            mUpdateView.text = "正在下载"
        }

        override fun downloading(max: Int, progress: Int) {
            // 标记为下载中
            mDownloading = true
            // 标记成未下载完成
            mDownloadComplete = false
            val curr = (progress / max.toDouble() * 100.0).toInt()
            mProgressView.progress = curr
            mUpdateView.text =
                String.format("下载中 %d%%", curr)

        }

        override fun done(apk: File?) {
            // 更新进度条
            mProgressView.progress = 0
            mProgressView.visibility = View.GONE

            mUpdateView.text = "下载完成，点击安装"
            // 标记当前不是下载中
            mDownloading = false
            // 标记成下载完成
            mDownloadComplete = true
            mApkFile = apk
        }

        override fun error(e: Exception?) {
            // 更新进度条
            mProgressView.progress = 0
            mProgressView.visibility = View.GONE
            // 标记当前不是下载中
            mDownloading = false
            mUpdateView.text = "下载失败，点击重试"
            // 删除下载的文件
            if (mApkFile != null && mApkFile!!.exists()) {
                mApkFile?.delete()
            }
        }

        override fun cancel() {
        }

    }
}