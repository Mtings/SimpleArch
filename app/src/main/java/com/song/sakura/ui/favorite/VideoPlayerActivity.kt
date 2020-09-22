package com.song.sakura.ui.favorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.song.sakura.R
import com.song.sakura.ui.base.IBaseActivity
import com.song.sakura.ui.base.IBaseViewModel
import com.song.sakura.widget.PlayerView
import com.ui.action.BundleAction
import com.ui.util.IntentBuilder
import kotlinx.android.synthetic.main.activity_video_player.*
import java.io.File

class VideoPlayerActivity : IBaseActivity<IBaseViewModel>(), PlayerView.onGoBackListener, BundleAction {

    companion object {
        fun start(context: Context, file: File?) {
            if (file == null || !file.isFile) {
                return
            }
            start(
                context,
                file.path,
                file.name
            )
        }

        fun start(context: Context, url: String?, title: String?) {
            if (TextUtils.isEmpty(url)) {
                return
            }
            val intent = Intent(context, VideoPlayerActivity::class.java)
            intent.putExtra(IntentBuilder.VIDEO, url)
            intent.putExtra(IntentBuilder.TITLE, title)
            context.startActivity(intent)
        }
    }

    override fun onClickGoBack(view: PlayerView?) {
        onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initImmersionBar()
        setContentView(R.layout.activity_video_player)
        init()
    }

    private fun init() {
        playerView.setOnGoBackListener(this)
        playerView.setGestureEnabled(true)
        playerView.setVideoTitle(getString(IntentBuilder.TITLE))
        playerView.setVideoSource(getString(IntentBuilder.VIDEO))
        playerView.start()
    }

    override fun onResume() {
        playerView.onResume()
        super.onResume()
    }

    override fun onPause() {
        playerView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        playerView.onDestroy()
        super.onDestroy()
    }

    override fun getBundle(): Bundle? {
        return intent.extras
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this).statusBarDarkFont(false).hideBar(BarHide.FLAG_HIDE_BAR).init()
    }
}