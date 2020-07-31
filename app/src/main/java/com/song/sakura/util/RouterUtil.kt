package com.song.sakura.util

import android.content.Context
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.song.sakura.entity.response.ArticleBean
import com.song.sakura.route.Router
import com.song.sakura.ui.web.WebViewActivity.Companion.MODE_SONIC
import com.song.sakura.ui.web.WebViewActivity.Companion.PARAM_MODE

object RouterUtil {
    @JvmOverloads
    fun navWebView(
        item: ArticleBean,
        context: Context,
        model: Int = MODE_SONIC,
        callback: ((Boolean) -> Unit)? = null //回调
    ) {
        val bundle = Bundle()
        bundle.putInt("articleId", item.id)
        bundle.putBoolean("collect", item.collect)
        bundle.putString("link", item.link)
        bundle.putInt(PARAM_MODE, model)
        ARouter.getInstance().build(Router.Main.webview)
            .with(bundle)
            .navigation(context)
    }

    @JvmOverloads
    fun navWebView(
        link: String,
        context: Context,
        model: Int = MODE_SONIC
    ) {
        val bundle = Bundle()
        bundle.putString("link", link)
        bundle.putInt(PARAM_MODE, model)
        ARouter.getInstance().build(Router.Main.webview)
            .with(bundle)
            .navigation(context)
    }

}