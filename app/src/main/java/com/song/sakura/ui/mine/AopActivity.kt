package com.song.sakura.ui.mine

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.hjq.permissions.Permission
import com.hjq.toast.ToastUtils
import com.song.sakura.R
import com.song.sakura.aop.CheckNet
import com.song.sakura.aop.Permissions
import com.song.sakura.aop.SingleClick
import com.song.sakura.ui.base.IBaseActivity
import com.song.sakura.ui.base.IBaseViewModel
import com.ui.action.ClickAction

class AopActivity : IBaseActivity<IBaseViewModel>(), ClickAction {

    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .titleBar(R.id.appbar)
            .keyboardEnable(true).statusBarDarkFont(true)
            .navigationBarWithKitkatEnable(false).init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aop)
        initImmersionBar()

        setOnClickListener(R.id.btn_dialog_check_net)
        setOnClickListener(R.id.btn_dialog_check_permission)
        setOnClickListener(R.id.btn_dialog_single_click)
        setOnClickListener(R.id.btn_activity_status)
        setOnClickListener(R.id.btn_activity_select_image)
    }

    @SingleClick
    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_dialog_check_net -> {
                checkNet()
            }
            R.id.btn_dialog_check_permission -> {
                requestPermission()
            }
            R.id.btn_dialog_single_click -> {
                singleClick()
            }
            R.id.btn_activity_status -> {
                startActivity(Intent(this, LottieActivity::class.java))
            }

            R.id.btn_activity_select_image -> {
                ImageSelectActivity.start(this, 4, object : OnPhotoSelectListener {
                    override fun onSelected(data: List<String?>?) {
                        ToastUtils.show("选择了$data")
                    }

                    override fun onCancel() {
                        ToastUtils.show("取消了")
                    }
                })
            }
        }
    }

    @CheckNet
    fun checkNet() {
        ToastUtils.show("网络连接正常")
    }

    @Permissions(
        Permission.READ_EXTERNAL_STORAGE,
        Permission.WRITE_EXTERNAL_STORAGE,
        Permission.CAMERA
    )
    fun requestPermission() {
        ToastUtils.show("已获取所有权限")
    }

    private fun singleClick() {
        ToastUtils.show("点击了按钮")
    }
}