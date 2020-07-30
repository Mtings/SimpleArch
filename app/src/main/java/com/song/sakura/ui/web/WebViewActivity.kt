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
import android.view.WindowManager
import android.webkit.*
import com.alibaba.android.arouter.facade.annotation.Route
import com.hjq.toast.ToastUtils
import com.song.sakura.R
import com.song.sakura.app.App
import com.song.sakura.route.Router
import com.song.sakura.ui.base.IBaseActivity
import com.song.sakura.ui.base.IBaseViewModel
import com.song.sakura.ui.base.ShareDialogFragment
import com.song.sakura.util.*
import com.song.sakura.vassonic.OfflinePkgSessionConnection
import com.song.sakura.vassonic.SonicJavaScriptInterface
import com.song.sakura.vassonic.SonicRuntimeImpl
import com.song.sakura.vassonic.SonicSessionClientImpl
import com.tencent.sonic.sdk.*
import com.ui.util.LogUtil
import kotlinx.android.synthetic.main.activity_webview.*

@Route(path = Router.Main.webview)
class WebViewActivity : IBaseActivity<WebViewModel>() {

    companion object {
        // 加载模式：MODE_DEFAULT 默认使用WebView加载；
        // MODE_SONIC 使用VasSonic框架加载；
        // MODE_SONIC_WITH_OFFLINE_CACHE 使用VasSonic框架离线加载
        const val MODE_DEFAULT = 0
        const val MODE_SONIC = 1
        const val MODE_SONIC_WITH_OFFLINE_CACHE = 2
        const val PARAM_MODE = "param_mode"

    }

    private var sonicSession: SonicSession? = null
    private var sonicSessionClient: SonicSessionClientImpl? = null
    private var mode: Int = MODE_DEFAULT

    var articleId = 0
    var collect = false
    var link: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParams()
        preloadInitVasSonic()
        setContentView(R.layout.activity_webview)

        mToolbar?.apply {
            inflateMenu(R.menu.share_menu)
            setOnMenuItemClickListener {
                ShareDialogFragment().showDialog(this@WebViewActivity, link ?: "")
                return@setOnMenuItemClickListener true
            }
        }

        initWebView()

        if (sonicSessionClient != null) {
            sonicSessionClient?.bindWebView(webView)
            sonicSessionClient?.clientReady()
        } else {
            webView.loadUrl(link)
        }
    }

    private fun initParams() {
        articleId = intent.getIntExtra("articleId", 0)
        collect = intent.getBooleanExtra("collect", false)
        link = intent.getStringExtra("link")
        mode = intent.getIntExtra(PARAM_MODE, MODE_DEFAULT)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webView.settings.apply {
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            /* 设置支持Js,必须设置*/
            javaScriptEnabled = true
            webView.removeJavascriptInterface("searchBoxJavaBridge_")
            intent.putExtra(
                SonicJavaScriptInterface.PARAM_LOAD_URL_TIME,
                System.currentTimeMillis()
            );
            webView.addJavascriptInterface(
                SonicJavaScriptInterface(sonicSessionClient, intent),
                "sonic"
            )
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
                    if (s.length > 10) {
                        mToolbar?.title = s.substring(0, 10) + "..."
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

            override fun shouldInterceptRequest(
                view: WebView?,
                url: String?
            ): WebResourceResponse? {
                if (sonicSession != null) {
                    val requestResponse = sonicSessionClient?.requestResource(url)
                    if (requestResponse is WebResourceResponse) return requestResponse
                }
                return null
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.startsWith("scheme:") || url.startsWith("scheme:")) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
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
                sonicSession?.sessionClient?.pageFinish(url)
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                handler?.proceed()
            }

            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                ToastUtils.show("加载失败")
                super.onReceivedError(view, errorCode, description, failingUrl)
            }

        }

        webView.setDownloadListener { url, _, _, _, _ ->
            // 调用系统浏览器下载
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    /**
     * 使用VasSonic框架提升H5首屏加载速度。
     */
    private fun preloadInitVasSonic() {
        window.addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)

        // init sonic engine if necessary, or maybe u can do this when application created
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(SonicRuntimeImpl(App.getApplication()), SonicConfig.Builder().build())
        }

        // if it's sonic mode , startup sonic session at first time
        if (MODE_DEFAULT != mode) { // sonic mode
            val sessionConfigBuilder = SonicSessionConfig.Builder()
            sessionConfigBuilder.setSupportLocalServer(true)

            // if it's offline pkg mode, we need to intercept the session connection
            if (MODE_SONIC_WITH_OFFLINE_CACHE == mode) {
                sessionConfigBuilder.setCacheInterceptor(object : SonicCacheInterceptor(null) {
                    override fun getCacheData(session: SonicSession): String? {
                        return null // offline pkg does not need cache
                    }
                })
                sessionConfigBuilder.setConnectionInterceptor(object :
                    SonicSessionConnectionInterceptor() {
                    override fun getConnection(
                        session: SonicSession,
                        intent: Intent
                    ): SonicSessionConnection {
                        return OfflinePkgSessionConnection(this@WebViewActivity, session, intent)
                    }
                })
            }

            // create sonic session and run sonic flow
            sonicSession =
                SonicEngine.getInstance().createSession(link ?: "", sessionConfigBuilder.build())
            if (null != sonicSession) {
                sonicSession?.bindClient(SonicSessionClientImpl().also { sonicSessionClient = it })
            } else {
                // this only happen when a same sonic session is already running,
                // u can comment following codes to feedback as a default mode.
                // throw new UnknownError("create session fail!");
                LogUtil.print("${title},${link ?: ""}:create sonic session fail!")
            }
        }
    }

    override fun onDestroy() {
        webView.destroy()
        sonicSession?.destroy()
        sonicSession = null
        super.onDestroy()
    }
}

class WebViewModel(app: Application) : IBaseViewModel(app) {

}