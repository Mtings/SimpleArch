package com.song.sakura.ui.mine

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

    override fun isImmersionBarEnabled(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aop)

        setOnClickListener(R.id.btn_dialog_check_net)
        setOnClickListener(R.id.btn_dialog_check_permission)
        setOnClickListener(R.id.btn_dialog_single_click)
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
        }
    }

    @CheckNet
    fun checkNet() {
        ToastUtils.show("网络连接正常")
    }

    @Permissions(Permission.MANAGE_EXTERNAL_STORAGE, Permission.CAMERA)
    fun requestPermission() {
        ToastUtils.show("已获取所有权限")
    }

    private fun singleClick() {
        ToastUtils.show("点击了按钮")
    }
}