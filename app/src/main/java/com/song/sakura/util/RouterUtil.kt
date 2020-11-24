package com.song.sakura.util

import android.content.Context
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.song.sakura.bean.response.ArticleBean
import com.song.sakura.route.Router

object RouterUtil {
    /**
     * 暂时不启用MODE_SONIC
     */
    @JvmOverloads
    fun navWebView(
        item: ArticleBean,
        context: Context,
        callback: ((Boolean) -> Unit)? = null //回调
    ) {
        val bundle = Bundle()
        bundle.putInt("articleId", item.id)
        bundle.putBoolean("collect", item.collect)
        bundle.putString("link", item.link)
        ARouter.getInstance().build(Router.Main.webview)
            .with(bundle)
            .navigation(context)
    }


    @JvmOverloads
    fun navWebView(
        link: String,
        context: Context
    ) {
        val bundle = Bundle()
        bundle.putString("link", link)
        ARouter.getInstance().build(Router.Main.webview)
            .with(bundle)
            .navigation(context)
    }

}