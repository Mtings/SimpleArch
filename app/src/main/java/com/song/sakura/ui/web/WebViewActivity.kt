package com.song.sakura.ui.web

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.*
import com.alibaba.android.arouter.facade.annotation.Route
import com.song.sakura.R
import com.song.sakura.extension.checkAppInstalled
import com.song.sakura.extension.openBrowser
import com.song.sakura.route.Router
import com.song.sakura.ui.base.IBaseActivity
import com.song.sakura.ui.base.IBaseViewModel
import com.song.sakura.ui.share.ShareDialogFragment
import kotlinx.android.synthetic.main.activity_webview.*

@Route(path = Router.Main.webview)
class WebViewActivity : IBaseActivity<WebViewModel>() {

    var articleId = 0
    var collect = false
    var link: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParams()
        setContentView(R.layout.activity_webview)
        initImmersionBar()

        mToolbar?.apply {
            inflateMenu(R.menu.share_menu)
            setOnMenuItemClickListener {
                ShareDialogFragment()
                    .showDialog(this@WebViewActivity, link ?: "")
                return@setOnMenuItemClickListener true
            }
        }

        initWebView()
        webView.loadUrl(link)
    }

    private fun initParams() {
        articleId = intent.getIntExtra("articleId", 0)
        collect = intent.getBooleanExtra("collect", false)
        link = intent.getStringExtra("link")
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webView.settings.apply {
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            /* 设置支持Js,必须设置*/
            javaScriptEnabled = true
            allowContentAccess = true
            databaseEnabled = true
            /* 大部分网页需要自己保存一些数据,这个时候就的设置下面这个属性 */
            domStorageEnabled = true
            setAppCacheEnabled(true)
            savePassword = false
            saveFormData = false
            /* 设置为使用webview推荐的窗口 */
            useWideViewPort = true
            /* 设置网页自适应屏幕大小 ---这个属性应该是跟上面一个属性一起用 */
            loadWithOverviewMode = true
            defaultTextEncodingName = "UTF-8"

            /* 设置WebView是否可以由JavaScript自动打开窗口，默认为false，通常与JavaScript的window.open()配合使用。*/
            javaScriptCanOpenWindowsAutomatically = true
            allowFileAccess = true
            /* HTML5的地理位置服务,设置为true,启用地理定位 */
            setGeolocationEnabled(true)
            /* 设置是否允许webview使用缩放的功能,我这里设为false,不允许 */
            builtInZoomControls = false
            /* 提高网页渲染的优先级 */
//            setRenderPriority(WebSettings.RenderPriority.HIGH)

        }

        /* 设置显示水平滚动条,就是网页右边的滚动条.我这里设置的不显示 */
        webView.isHorizontalScrollBarEnabled = false
        /* 指定垂直滚动条是否有叠加样式 */
        webView.setVerticalScrollbarOverlay(true)
        /* 设置滚动条的样式 */
        webView.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        /* 重写WebChromeClient监听网页加载的进度,从而实现进度条 */
        webView.webChromeClient = object : WebChromeClient() {

            override fun onReceivedTitle(webView: WebView, s: String) {
                super.onReceivedTitle(webView, s)

                if (!TextUtils.isEmpty(title)
                    && !title.contains("html")
                    && !title.contains("/")
                    && !title.contains("?")
                ) {
                    if (s.length > 15) {
                        mToolbar?.title = s.substring(0, 15) + "..."
                    }
                }
            }

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progress.progress = newProgress
            }

        }
        /* 同上,重写WebViewClient可以监听网页的跳转和资源加载等等... */
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.startsWith("scheme:") || url.startsWith("scheme:")) {
                    url.openBrowser(this@WebViewActivity)
                    return true
                }
                if (url.checkAppInstalled(this@WebViewActivity) && url.startsWith("bilibili://video")) {
                    url.openBrowser(this@WebViewActivity)
                    return true
                }
                val isHttp =
                    url.startsWith("http://") || url.startsWith("https://")
                if (!isHttp) {
                    //拦截非http，https地址
                    return true
                } else {
                    link = url
                }
                return false
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                progress.visibility = View.VISIBLE
                link = url
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView, url: String) {
                progress.visibility = View.GONE
                super.onPageFinished(view, url)
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                handler?.proceed()
            }

        }

        webView.setDownloadListener { url, _, _, _, _ ->
            // 调用系统浏览器下载
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            finish()
        }
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }
}

class WebViewModel(app: Application) : IBaseViewModel(app) {

}