package com.song.sakura.ui.favorite

import android.app.Activity
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.hjq.toast.ToastUtils
import com.song.sakura.BuildConfig
import com.song.sakura.R
import com.song.sakura.aop.Permissions
import com.song.sakura.ui.base.IBaseActivity
import com.song.sakura.ui.base.IBaseViewModel
import com.ui.action.BundleAction
import com.ui.base.BaseActivity
import com.ui.util.IntentBuilder
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : IBaseActivity<IBaseViewModel>(), BundleAction {

    companion object {
        const val CAMERA_REQUEST_CODE = 1024

        fun start(activity: BaseActivity, listener: OnCameraListener?) {
            start(activity, false, listener)
        }

        @Permissions(Permission.MANAGE_EXTERNAL_STORAGE, Permission.CAMERA)
        fun start(activity: BaseActivity, video: Boolean, listener: OnCameraListener?) {
            val file: File? = createCameraFile(video)
            val intent = Intent(activity, CameraActivity::class.java)
            intent.putExtra(IntentBuilder.FILE, file)
            intent.putExtra(IntentBuilder.VIDEO, video)
            activity.startActivityForResult(intent) { resultCode: Int, data: Intent? ->
                if (listener == null) {
                    return@startActivityForResult
                }
                if (resultCode == Activity.RESULT_OK) {
                    listener.onSelected(file)
                } else {
                    listener.onCancel()
                }
            }
        }

        /**
         * 创建一个拍照图片文件对象
         */
        private fun createCameraFile(video: Boolean): File? {
            var folder = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera")
            if (!folder.exists() || !folder.isDirectory) {
                if (!folder.mkdirs()) {
                    folder = Environment.getExternalStorageDirectory()
                }
            }
            return try {
                val file = File(
                    folder,
                    (if (video) "IMG_" else "VID") + SimpleDateFormat(
                        "_yyyyMMdd_HHmmss.",
                        Locale.getDefault()
                    ).format(Date()) + if (video) "mp4" else "jpg"
                )
                file.createNewFile()
                file
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    private var mFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    private fun initData() {
        // 启动系统相机
        val intent: Intent = if (getBoolean(IntentBuilder.VIDEO)) {
            // 录制视频
            Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        } else {
            // 拍摄照片
            Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        }
        if (XXPermissions.isGrantedPermission(
                this,
                mutableListOf(
                    Permission.MANAGE_EXTERNAL_STORAGE,
                    Permission.CAMERA
                )
            ) && intent.resolveActivity(packageManager) != null
        ) {
            mFile = getSerializable(IntentBuilder.FILE)
            if (mFile != null && mFile!!.exists()) {
                val imageUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // 通过 FileProvider 创建一个 Content 类型的 Uri 文件
                    FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", mFile!!)
                } else {
                    Uri.fromFile(mFile)
                }
                // 对目标应用临时授权该 Uri 所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                // 将拍取的照片保存到指定 Uri
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            } else {
                ToastUtils.show(R.string.camera_image_error)
            }
        } else {
            ToastUtils.show(R.string.camera_launch_fail)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE) {
            when (resultCode) {
                // 重新扫描多媒体（否则可能扫描不到）
                Activity.RESULT_OK ->
                    MediaScannerConnection.scanFile(applicationContext, arrayOf(mFile?.path), null, null)
                // 删除这个文件
                Activity.RESULT_CANCELED -> mFile?.delete()
                else -> {
                }
            }
            setResult(resultCode)
            finish()
        }
    }


    override fun getBundle(): Bundle? {
        return intent.extras
    }
}

/**
 * 拍照选择监听
 */
interface OnCameraListener {
    /**
     * 选择回调
     *
     * @param file 文件
     */
    fun onSelected(file: File?)

    /**
     * 取消回调
     */
    fun onCancel() {}
}

