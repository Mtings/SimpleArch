package com.song.sakura.ui.favorite

import android.app.Activity
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
            var urlList = urls
            if (urlList.size > 2500) {
                // 请注意：如果传输的数据量过大，会抛出此异常，并且这种异常是不能被捕获的
                // 所以当图片数量过多的时候，我们应当只显示一张，这种一般是手机图片过多导致的
                // 经过测试，传入 3121 张图片集合的时候会抛出此异常，所以保险值应当是 2500
                // android.os.TransactionTooLargeException: data parcel size 521984 bytes
                urlList = Collections.singletonList(urlList[index]) as ArrayList<String>
            }
            intent.putExtra(IntentBuilder.IMAGE, urlList)
            intent.putExtra(IntentBuilder.INDEX, index)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
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