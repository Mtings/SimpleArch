package com.song.sakura.ui.launch

import android.os.Bundle
import com.gyf.immersionbar.ImmersionBar
import com.song.sakura.R
import com.song.sakura.ui.base.IBaseActivity
import com.song.sakura.ui.base.IBaseViewModel
import com.song.sakura.ui.main.MainActivity.Companion.goMain
import kotlinx.coroutines.*

class LaunchActivity : IBaseActivity<IBaseViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        window.setBackgroundDrawableResource(R.drawable.bg_launch)
        super.onCreate(savedInstanceState)
        initImmersionBar()
        if (!isTaskRoot) {
            finish()
            return
        }

        GlobalScope.launch {
            delay(1000L)
            goMain(this@LaunchActivity)
        }

    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .fitsSystemWindows(true)
            .transparentStatusBar()
            .statusBarDarkFont(false).init()
    }

    override fun onBackPressed() {
        //禁用onBackPressed()
    }
}

