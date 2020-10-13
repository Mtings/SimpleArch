package com.song.sakura.ui.mine

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.song.sakura.R
import com.song.sakura.aop.CheckNet
import com.song.sakura.ui.base.IBaseActivity
import com.song.sakura.ui.base.IBaseViewModel
import com.song.sakura.ui.base.ImagePagerAdapter
import com.ui.action.BundleAction
import com.ui.util.IntentBuilder
import kotlinx.android.synthetic.main.activity_image_preview.*
import java.util.*

class ImagePreviewActivity : IBaseActivity<IBaseViewModel>(), BundleAction {

    companion object {
        fun start(context: Context, url: String) {
            val images = ArrayList<String>(1)
            images.add(url)
            start(context, images)
        }

        fun start(context: Context, urls: ArrayList<String>) {
            start(context, urls, 0)
        }

        @CheckNet
        fun start(context: Context, urls: ArrayList<String>, index: Int) {
            val intent = Intent(context, ImagePreviewActivity::class.java)
            intent.putExtra(IntentBuilder.IMAGE, urls)
            intent.putExtra(IntentBuilder.INDEX, index)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initImmersionBar()
        setContentView(R.layout.activity_image_preview)
        initView()
        initData()
    }

    private fun initView() {
        pageIndicatorView.setViewPager(viewPager)
    }

    private fun initData() {
        val images = getStringArrayList(IntentBuilder.IMAGE)
        val index = getInt(IntentBuilder.INDEX)
        if (!images.isNullOrEmpty()) {
            viewPager.adapter = ImagePagerAdapter(this, images)
            if (index != 0 && index <= images.size) {
                viewPager.currentItem = index
            }
        } else {
            finish()
        }
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this).statusBarDarkFont(false).hideBar(BarHide.FLAG_HIDE_BAR).init()
    }

    override fun getBundle(): Bundle? {
        return intent.extras
    }

}