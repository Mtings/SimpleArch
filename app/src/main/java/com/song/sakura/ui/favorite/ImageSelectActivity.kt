package com.song.sakura.ui.favorite

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.TypedValue
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.hjq.toast.ToastUtils
import com.song.sakura.R
import com.song.sakura.action.StatusAction
import com.song.sakura.aop.Permissions
import com.song.sakura.helper.DoubleClickHelper
import com.song.sakura.ui.base.BaseViewHolder
import com.song.sakura.ui.base.IBaseActivity
import com.song.sakura.ui.base.IBaseViewModel
import com.song.sakura.ui.dialog.AlbumDialog
import com.song.sakura.widget.GridSpaceDecoration
import com.song.sakura.widget.HintLayout
import com.ui.action.BundleAction
import com.ui.action.ClickAction
import com.ui.base.BaseActivity
import com.ui.util.IntentBuilder
import com.ui.util.RxUtil
import kotlinx.android.synthetic.main.activity_select_image.*
import kotlinx.android.synthetic.main.item_image_select.view.*
import kotlinx.coroutines.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class ImageSelectActivity : IBaseActivity<IBaseViewModel>(), StatusAction, ClickAction, BundleAction,
    Toolbar.OnMenuItemClickListener {

    private lateinit var mAdapter: ImageSelectAdapter

    /** 最大选中  */
    private var mMaxSelect = 1

    /** 选中列表  */
    private val mSelectImage = ArrayList<String>()

    /** 全部图片  */
    private val mAllImage = ArrayList<String>()

    /** 图片专辑  */
    private val mAllAlbum = HashMap<String, MutableList<String>>()

    companion object {
        fun start(activity: BaseActivity, listener: OnPhotoSelectListener?) {
            start(activity, 1, listener)
        }

        @Permissions(Permission.MANAGE_EXTERNAL_STORAGE)
        fun start(activity: BaseActivity, maxSelect: Int, listener: OnPhotoSelectListener?) {
            require(maxSelect >= 1) {
                // 最少要选择一个图片
                "are you ok?"
            }
            val intent = Intent(activity, ImageSelectActivity::class.java)
            intent.putExtra(IntentBuilder.AMOUNT, maxSelect)
            activity.startActivityForResult(intent) { resultCode: Int, data: Intent? ->
                if (listener == null || data == null) {
                    return@startActivityForResult
                }
                if (resultCode == RESULT_OK) {
                    listener.onSelected(data.getStringArrayListExtra(IntentBuilder.IMAGE))
                } else {
                    listener.onCancel()
                }
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (DoubleClickHelper.isSingleClick()) {
            if (mAllImage.isEmpty()) {
                return true
            }
            val data: ArrayList<AlbumDialog.AlbumInfo> = ArrayList()
            var totalAmount = 0
            val keys: Set<String> = mAllAlbum.keys
            for (key in keys) {
                val temp: List<String>? = mAllAlbum[key]
                if (!temp.isNullOrEmpty()) {
                    totalAmount += temp.size
                    data.add(
                        AlbumDialog.AlbumInfo(
                            temp[0],
                            key,
                            String.format(getString(R.string.image_select_total), temp.size),
                            mAdapter.data == temp
                        )
                    )
                }
            }
            data.add(
                0,
                AlbumDialog.AlbumInfo(
                    mAllImage[0],
                    getString(R.string.image_select_all),
                    String.format(getString(R.string.image_select_total), totalAmount),
                    mAdapter.data == mAllImage
                )
            )
            AlbumDialog.Builder(this@ImageSelectActivity)
                .setData(data)
                .setListener { _, position, bean ->
                    mToolbar?.apply {
                        clearMenu()
                        addTextRight(bean.name)
                    }
                    // 滚动回第一个位置
                    list.scrollToPosition(0)
                    if (position == 0) {
                        mAdapter.setList(mAllImage)
                    } else {
                        mAdapter.setList(mAllAlbum[bean.name])
                    }
                    // 执行列表动画
                    list.layoutAnimation = AnimationUtils.loadLayoutAnimation(activity, R.anim.layout_from_right)
                    list.scheduleLayoutAnimation()
                }.show()
        }
        return true
    }

    override fun isImmersionBarEnabled(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_image)

        mAdapter = ImageSelectAdapter(mSelectImage)
        list.adapter = mAdapter
        list.itemAnimator = null // 禁用动画效果
        list.addItemDecoration(
            GridSpaceDecoration(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, resources.displayMetrics).toInt()
            )
        ) // 添加分割线

        mAdapter.setOnItemClickListener { _, _, position ->
            if (mSelectImage.contains(mAdapter.getItem(position))) {
                ImagePreviewActivity.start(
                    activity,
                    mSelectImage,
                    mSelectImage.indexOf(mAdapter.getItem(position))
                )
            } else {
                ImagePreviewActivity.start(activity, mAdapter.getItem(position))
            }
        }

        mAdapter.setOnItemLongClickListener { _, view, _ ->
            return@setOnItemLongClickListener if (mSelectImage.size < mMaxSelect) {
                // 长按的时候模拟选中
                view.findViewById<FrameLayout>(R.id.fl_image_select_check).performClick()
            } else {
                false
            }
        }

        mAdapter.addChildClickViewIds(R.id.fl_image_select_check)
        mAdapter.setOnItemChildClickListener { _, view, position ->
            if (view.id == R.id.fl_image_select_check) {
                if (mSelectImage.contains(mAdapter.getItem(position))) {
                    mSelectImage.remove(mAdapter.getItem(position))
                    if (mSelectImage.isEmpty()) {
                        floating.hide()
                        postDelayed({
                            floating.setImageResource(R.drawable.ic_camera)
                            floating.show()
                        }, 200)
                    }
                } else {
                    if (mMaxSelect == 1 && mSelectImage.size == 1) {
                        val data: List<String> = mAdapter.data
                        if (data != null) {
                            val index = data.indexOf(mSelectImage[0])
                            if (index != -1) {
                                mSelectImage.removeAt(0)
                                mAdapter.notifyItemChanged(index)
                            }
                        }
                        mSelectImage.add(mAdapter.getItem(position))
                    } else if (mSelectImage.size < mMaxSelect) {
                        mSelectImage.add(mAdapter.getItem(position))
                        if (mSelectImage.size == 1) {
                            floating.hide()
                            postDelayed({
                                floating.setImageResource(R.drawable.ic_succeed)
                                floating.show()
                            }, 200)
                        }
                    } else {
                        ToastUtils.show(String.format(getString(R.string.image_select_max_hint), mMaxSelect))
                    }
                }
                mAdapter.notifyItemChanged(position)
            }
        }

        bindUi(RxUtil.click(floating)) {
            if (mSelectImage.isEmpty()) {
                // 点击拍照
                CameraActivity.start(this, object : OnCameraListener {
                    override fun onSelected(file: File?) {
                        if (file != null) {
                            // 当前选中图片的数量必须小于最大选中数
                            if (mSelectImage.size < mMaxSelect) {
                                mSelectImage.add(file.path)
                            }

                            CoroutineScope(Dispatchers.Main).launch {
                                // 这里需要延迟刷新，否则可能会找不到拍照的图片
                                delay(1000L)
                                // 重新加载图片列表
                                run()
                            }
                        }
                    }
                })
            } else {
                // 完成选择
                setResult(RESULT_OK, Intent().putStringArrayListExtra(IntentBuilder.IMAGE, mSelectImage))
                finish()


            }
        }

        mToolbar?.apply {
            title = "图片选择"
            addTextRight("所有图片")
        }?.setOnMenuItemClickListener(this)

        // 获取最大的选择数
        mMaxSelect = getInt(IntentBuilder.AMOUNT, mMaxSelect)

        // 显示加载进度条
        showLoading()
        // 加载图片列表
        CoroutineScope(Dispatchers.Main).launch {
            run()
        }
    }

    private suspend fun run() {
        withContext(Dispatchers.IO) {
            mAllAlbum.clear()
            mAllImage.clear()
            val contentUri = MediaStore.Files.getContentUri("external")
            val sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC"
            val selection =
                "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)" + " AND " + MediaStore.MediaColumns.SIZE + ">0"
            val contentResolver = contentResolver
            val projections = arrayOf(
                MediaStore.Files.FileColumns._ID, MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED,
                MediaStore.MediaColumns.MIME_TYPE, MediaStore.MediaColumns.WIDTH,
                MediaStore.MediaColumns.HEIGHT, MediaStore.MediaColumns.SIZE
            )

            var cursor: Cursor? = null
            if (XXPermissions.isGrantedPermission(this@ImageSelectActivity, Permission.MANAGE_EXTERNAL_STORAGE)) {
                cursor = contentResolver.query(
                    contentUri,
                    projections,
                    selection,
                    arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString()),
                    sortOrder
                )
            }
            if (cursor != null && cursor.moveToFirst()) {
                val pathIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATA)
                val mimeTypeIndex = cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)
                val sizeIndex = cursor.getColumnIndex(MediaStore.MediaColumns.SIZE)
                do {
                    val size = cursor.getLong(sizeIndex)
                    // 图片大小不得小于 1 KB
                    if (size < 1024) {
                        continue
                    }
                    val type = cursor.getString(mimeTypeIndex)
                    val path = cursor.getString(pathIndex)
                    if (TextUtils.isEmpty(path) || TextUtils.isEmpty(type)) {
                        continue
                    }
                    val file = File(path)
                    if (!file.exists() || !file.isFile) {
                        continue
                    }
                    val parentFile = file.parentFile
                    if (parentFile != null) {
                        // 获取目录名作为专辑名称
                        val albumName = parentFile.name
                        var data: MutableList<String>? = mAllAlbum[albumName]
                        if (data == null) {
                            data = java.util.ArrayList()
                            mAllAlbum[albumName] = data
                        }
                        data.add(path)
                        mAllImage.add(path)
                    }
                } while (cursor.moveToNext())

                cursor.close()
            }
            delay(500L)
            withContext(Dispatchers.Main) {
                // 滚动回第一个位置
                list.scrollToPosition(0)
                // 设置新的列表数据
                mAdapter.setList(mAllImage)
                if (mSelectImage.isEmpty()) {
                    floating.setImageResource(R.drawable.ic_camera)
                } else {
                    floating.setImageResource(R.drawable.ic_succeed)
                }

                // 执行列表动画
                list.layoutAnimation = AnimationUtils.loadLayoutAnimation(activity, R.anim.layout_fall_down)
                list.scheduleLayoutAnimation()

                mToolbar?.apply {
                    clearMenu()
                    addTextRight("所有图片")
                }

                if (mAllImage.isEmpty()) {
                    // 显示空布局
                    showEmpty()
                } else {
                    // 显示加载完成
                    showComplete()
                }
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        // 遍历判断选择了的图片是否被删除了
        for (path in mSelectImage) {
            val file = File(path)
            if (!file.isFile) {
                mSelectImage.remove(path)
                mAllImage.remove(path)
                val parentFile = file.parentFile
                if (parentFile != null) {
                    val data = mAllAlbum[parentFile.name]
                    data?.remove(path)
                    mAdapter.notifyDataSetChanged()
                    if (mSelectImage.isEmpty()) {
                        floating.setImageResource(R.drawable.ic_camera)
                    } else {
                        floating.setImageResource(R.drawable.ic_succeed)
                    }
                }
            }
        }
    }

    override fun getHintLayout(): HintLayout {
        return hintStatusLayout
    }

    override fun getBundle(): Bundle? {
        return intent.extras
    }

}

class ImageSelectAdapter(mSelectImages: List<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_image_select, null) {

    private var selectImages: List<String> = mSelectImages

    override fun convert(holder: BaseViewHolder, item: String) {
        Glide.with(context)
            .load(item)
            .into(holder.itemView.imageView)

        holder.itemView.checkBox.isChecked = selectImages.contains(item)
    }

}

/**
 * 图片选择监听
 */
interface OnPhotoSelectListener {
    /**
     * 选择回调
     *
     * @param data          图片列表
     */
    fun onSelected(data: List<String?>?)

    /**
     * 取消回调
     */
    fun onCancel() {}
}