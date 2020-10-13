package com.song.sakura.ui.mine

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.song.sakura.R
import com.song.sakura.action.StatusAction
import com.song.sakura.ui.base.IBaseActivity
import com.song.sakura.ui.base.IBaseViewModel
import com.song.sakura.ui.dialog.MenuDialog
import com.song.sakura.widget.HintLayout
import com.ui.action.HandlerAction
import kotlinx.android.synthetic.main.activity_status.*

class LottieActivity : IBaseActivity<IBaseViewModel>(), StatusAction, HandlerAction {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)
        initImmersionBar()
        mToolbar?.apply {
            title = "界面状态"
        }

        MenuDialog.Builder(this) //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
            .setList("加载中", "请求错误", "空数据提示", "自定义提示")
            .setListener { _, position, _ ->
                when (position) {
                    0 -> {
                        showLoading()
                        postDelayed(this::showComplete, 5000)
                    }
                    1 -> showError { v ->
                        showLoading()
                        postDelayed(this::showEmpty, 2500)
                    }
                    2 -> showEmpty()
                    3 -> showLayout(ContextCompat.getDrawable(activity, R.drawable.ic_finish), "暂无数据") {
                        showLoading()
                        postDelayed(this::showEmpty, 2500)
                    }
                    else -> {
                    }
                }
            }.show()
    }

    override fun getHintLayout(): HintLayout {
        return hintStatusLayout
    }

}