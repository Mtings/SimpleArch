package com.song.sakura.ui.mine

import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.song.sakura.R
import com.song.sakura.ui.base.IBaseActivity
import com.song.sakura.ui.base.IBaseViewModel
import com.ui.action.ClickAction
import kotlinx.android.synthetic.main.activity_coordinatorlayout_header.*

class MotionLayoutActivity : IBaseActivity<IBaseViewModel>(), ClickAction {

    override fun initImmersionBar() {
        ImmersionBar.with(this).keyboardEnable(true)
//            .titleBar(R.id.appbar)
            .hideBar(BarHide.FLAG_HIDE_STATUS_BAR)
            .statusBarDarkFont(false)
            .init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordinatorlayout)
        initImmersionBar()
//        ImmersionBar.with(this).titleBar(titleBar).init()
//        ViewCompat.getWindowInsetsController(window.decorView)?.hide(WindowInsetsCompat.Type.statusBars())
//        setSupportActionBar(titleBar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
}