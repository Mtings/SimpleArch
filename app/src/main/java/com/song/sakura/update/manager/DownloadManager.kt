package com.song.sakura.update.manager

import android.app.NotificationChannel
import android.content.Context
import android.text.TextUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.hjq.permissions.Permission
import com.song.sakura.aop.CheckNet
import com.song.sakura.aop.Permissions
import com.song.sakura.update.UpdateDialog
import com.song.sakura.update.listener.OnDownloadListener
import com.song.sakura.util.ApkUtil
import java.lang.ref.SoftReference
import java.util.*


class DownloadManager {

    companion object {
        /*** apk文件后缀 */
        const val APK_SUFFIX = ".apk"
        const val TAG: String = "UpdateApp: DownloadManager"

        private var context: SoftReference<Context>? = null
        var manager: DownloadManager? = null

        fun getInstance(context: Context): DownloadManager {
            this.context = SoftReference<Context>(context)
            return manager ?: synchronized(this) {
                manager ?: DownloadManager().also { manager = it }
            }
        }
    }

    /**
     * 下载过程回调
     */
    private var onDownloadListeners: MutableList<OnDownloadListener> = ArrayList<OnDownloadListener>()

    /*** apk下载到的位置路径 */
    private var downloadPath: String = ""

    /*** 要更新apk的下载地址 */
    private var apkUrl: String = ""

    /*** apk下载好的名字 .apk 结尾 */
    private var apkName: String = ""

    /*** 要更新apk的versionCode */
    private var apkVersionCode = Int.MIN_VALUE

    /*** 显示给用户的版本号 */
    private var apkVersionName = ""

    /*** 更新描述 */
    private var apkDescription = ""

    /*** 是否提示用户 "当前已是最新版本" */
    private var showNewerToast: Boolean = false

    /*** 通知栏的图标 资源路径 */
    private var smallIcon = -1

    /*** 安装包大小 单位 M */
    private var apkSize = ""

    /*** 新安装包md5文件校验（32位)，校验重复下载 */
    private var apkMD5 = ""

    /*** 下载完成是否自动弹出安装页面 (默认为true) */
    private var jumpInstallPage = true

    /*** 是否强制升级(默认为false) */
    private var forcedUpgrade = false

    /*** 通知栏id */
    private var notifyId = 1011

    /*** 适配Android O的渠道通知 */
    private var notificationChannel: NotificationChannel? = null

    /*** 当前下载状态 */
    private var state = false

    /*** 兼容Android N Uri 授权 */
    private var AUTHORITIES: String = ""

    /*** 下载管理 */
    private var httpManager: BaseHttpDownloadManager? = null

    /*** 设置下载监听器 */
    fun setOnDownloadListener(onDownloadListener: OnDownloadListener): DownloadManager {
        onDownloadListeners.add(onDownloadListener)
        return this
    }

    /*** 获取下载监听器 */
    fun getOnDownloadListener(): MutableList<OnDownloadListener> {
        return onDownloadListeners
    }

    /*** 获取apk下载地址 */
    fun getApkUrl(): String {
        return apkUrl
    }

    /*** 设置apk下载地址 */
    fun setApkUrl(apkUrl: String): DownloadManager {
        this.apkUrl = apkUrl
        return this
    }

    /*** 获取下载好的名字 .apk 结尾 */
    fun getApkName(): String {
        return apkName
    }

    /*** 设置下载好的名字 .apk 结尾 */
    fun setApkName(apkName: String): DownloadManager {
        this.apkName = apkName
        return this
    }

    /*** 获取apk的VersionCode */
    fun getApkVersionCode(): Int {
        return apkVersionCode
    }

    /*** 设置apk的VersionCode */
    fun setApkVersionCode(apkVersionCode: Int): DownloadManager {
        this.apkVersionCode = apkVersionCode
        return this
    }

    /*** 获取apk的versionName */
    fun getApkVersionName(): String? {
        return apkVersionName
    }

    /*** 设置apk的versionName */
    fun setApkVersionName(apkVersionName: String): DownloadManager {
        this.apkVersionName = apkVersionName
        return this
    }

    /*** 获取新版本描述信息 */
    fun getApkDescription(): String {
        return apkDescription
    }

    /*** 设置新版本描述信息 */
    fun setApkDescription(apkDescription: String): DownloadManager {
        this.apkDescription = apkDescription
        return this
    }

    /*** 获取新版本文件大小 */
    fun getApkSize(): String {
        return apkSize
    }

    /*** 设置新版本文件大小 */
    fun setApkSize(apkSize: String): DownloadManager {
        this.apkSize = apkSize
        return this
    }

    /*** 新安装包md5文件校验 */
    fun setApkMD5(apkMD5: String): DownloadManager {
        this.apkMD5 = apkMD5
        return this
    }

    /*** 新安装包md5文件校验 */
    fun getApkMD5(): String {
        return apkMD5
    }

    /*** 设置是否提示用户"当前已是最新版本" */
    fun setShowNewerToast(showNewerToast: Boolean): DownloadManager {
        this.showNewerToast = showNewerToast
        return this
    }

    /*** 获取是否提示用户"当前已是最新版本" */
    fun isShowNewerToast(): Boolean {
        return showNewerToast
    }

    /*** 获取通知栏图片资源id */
    fun getSmallIcon(): Int {
        return smallIcon
    }

    /*** 设置通知栏图片资源id */
    fun setSmallIcon(smallIcon: Int): DownloadManager {
        this.smallIcon = smallIcon
        return this
    }

    /*** 设置当前状态 */
    fun setState(state: Boolean) {
        this.state = state
    }

    /*** 当前是否正在下载 */
    fun isDownloading(): Boolean {
        return state
    }

    /*** 设置 下载完成是否自动弹出安装页面 (默认为true) */
    fun setJumpInstallPage(jumpInstallPage: Boolean): DownloadManager {
        this.jumpInstallPage = jumpInstallPage
        return this
    }

    /*** 获取 下载完成是否自动弹出安装页面 (默认为true) */
    fun isJumpInstallPage(): Boolean {
        return jumpInstallPage
    }

    /*** 设置 是否强制升级(默认为false) */
    fun setForcedUpgrade(forcedUpgrade: Boolean): DownloadManager {
        this.forcedUpgrade = forcedUpgrade
        return this
    }

    /*** 获取 是否强制升级(默认为false) */
    fun isForcedUpgrade(): Boolean {
        return forcedUpgrade
    }

    /*** 设置通知栏消息id*/
    fun setNotifyId(notifyId: Int): DownloadManager {
        this.notifyId = notifyId
        return this
    }

    /*** 获取通知栏消息id */
    fun getNotifyId(): Int {
        return notifyId
    }

    /*** 设置Android O的通知渠道 */
    fun setNotificationChannel(notificationChannel: NotificationChannel?): DownloadManager {
        this.notificationChannel = notificationChannel
        return this
    }

    /**
     * 获取Android O的通知渠道
     */
    fun getNotificationChannel(): NotificationChannel? {
        return notificationChannel
    }

    /*** 设置下载管理器 */
    fun setHttpManager(httpManager: BaseHttpDownloadManager): DownloadManager {
        this.httpManager = httpManager
        return this;
    }

    /*** 获取下载管理器 */
    fun getHttpManager(): BaseHttpDownloadManager? {
        return this.httpManager
    }

    /*** 开始下载 */
    @CheckNet
    @Permissions(Permission.MANAGE_EXTERNAL_STORAGE)
    fun download() {
        if (!checkParams()) {
            return
        }
        //对版本进行判断，是否显示升级对话框
        if (getApkVersionCode() > ApkUtil.getVersionCode(context?.get())) {
            UpdateDialog.Builder(context?.get()!!)
                // 版本名
                .setVersionName(getApkVersionName().toString())
                // apk大小
                .setApkSize(getApkSize())
                // 是否强制更新
                .setForceUpdate(isForcedUpgrade())
                // 更新日志
                .setUpdateLog(getApkDescription())
                .show()
        } else {
            if (showNewerToast) {
                ToastUtils.showShort("当前已是最新版本!")
            }
            LogUtils.eTag(TAG, "当前已是最新版本")
        }

    }

    private fun checkParams(): Boolean {
        if (TextUtils.isEmpty(apkUrl)) {
            LogUtils.eTag(TAG, "apkUrl can not be empty!")
            return false
        }
        if (TextUtils.isEmpty(apkName)) {
            LogUtils.eTag(TAG, "apkName can not be empty!")
            return false
        }
        if (!apkName.endsWith(APK_SUFFIX)) {
            LogUtils.eTag(TAG, "apkName must endsWith .apk!")
            return false
        }
        downloadPath = context?.get()?.externalCacheDir!!.path
        AUTHORITIES = context?.get()?.packageName + ".provider"
        return true
    }

    fun getDownLoadPath(): String {
        return downloadPath
    }

    fun getAuthorities(): String {
        return AUTHORITIES
    }

    /**
     * 释放资源
     */
    fun release() {
        context?.clear()
        context = null
        manager?.getOnDownloadListener()?.clear()
        manager = null
    }

}