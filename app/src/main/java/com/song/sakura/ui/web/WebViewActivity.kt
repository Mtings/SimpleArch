package com.song.sakura.ui.web

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.webkit.*
import android.webkit.WebChromeClient.FileChooserParams
import com.alibaba.android.arouter.facade.annotation.Route
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.hjq.toast.ToastUtils
import com.song.sakura.R
import com.song.sakura.extension.checkAppInstalled
import com.song.sakura.extension.openBrowser
import com.song.sakura.route.Router
import com.song.sakura.ui.base.IBaseActivity
import com.song.sakura.ui.base.IBaseViewModel
import com.song.sakura.ui.dialog.MessageDialog
import com.song.sakura.ui.share.ShareDialogFragment
import com.ui.base.BaseActivity
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
        webView.loadUrl(link ?: "about:blank")
    }

    private fun initParams() {
        articleId = intent.getIntExtra("articleId", 0)
        collect = intent.getBooleanExtra("collect", false)
        link = intent.getStringExtra("link")
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webView.settings.apply {
            /* 设置支持Js,必须设置*/
            javaScriptEnabled = true
            allowContentAccess = true
            databaseEnabled = true
            // 本地 DOM 存储（解决加载某些网页出现白板现象）
            domStorageEnabled = true
            setAppCacheEnabled(true)
            // 允许保存密码
            savePassword = false
            saveFormData = false
            /* 设置为使用webview推荐的窗口 */
            useWideViewPort = true
            /* 设置网页自适应屏幕大小 ---这个属性应该是跟上面一个属性一起用 */
            loadWithOverviewMode = true
            defaultTextEncodingName = "UTF-8"
            // 加快网页加载完成的速度，等页面完成再加载图片
            loadsImagesAutomatically = true
            /* 设置WebView是否可以由JavaScript自动打开窗口，默认为false，通常与JavaScript的window.open()配合使用。*/
            javaScriptCanOpenWindowsAutomatically = true
            // 允许文件访问
            allowFileAccess = true
            /* HTML5的地理位置服务,设置为true,启用地理定位 */
            setGeolocationEnabled(true)
            /* 设置是否允许webview使用缩放的功能,我这里设为false,不允许 */
            builtInZoomControls = false
            /* 提高网页渲染的优先级 */
//            setRenderPriority(WebSettings.RenderPriority.HIGH)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // 解决 Android 5.0 上 WebView 默认不允许加载 Http 与 Https 混合内容
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }

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
                        mToolbar?.title = Html.fromHtml(s.substring(0, 15) + "...")
                    }
                }
            }

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progress.progress = newProgress
            }

            override fun onShowFileChooser(
                mWebView: WebView?,
                callback: ValueCallback<Array<Uri?>?>,
                params: FileChooserParams?
            ): Boolean {
                XXPermissions.with(this@WebViewActivity)
                    .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                    .request(object : OnPermissionCallback {
                        override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                            if (all && params != null) {
                                openSystemFileChooser(this@WebViewActivity, callback, params)
                            } else {
                                callback.onReceiveValue(null)
                            }
                        }

                        override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                            callback.onReceiveValue(null)
                            if (never) {
                                ToastUtils.show("授权失败，请手动授予权限")
                                XXPermissions.startPermissionActivity(this@WebViewActivity, permissions)
                            } else {
                                ToastUtils.show("请先授予权限")
                            }
                        }
                    })
                return true
            }

        }
        /* 同上,重写WebViewClient可以监听网页的跳转和资源加载等等... */
        webView.webViewClient = object : WebViewClient() {

            @TargetApi(Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return shouldOverrideUrlLoading(view, request?.url.toString())
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                if (url.startsWith("scheme:") || url.startsWith("scheme:")) {
                    url.openBrowser(this@WebViewActivity)
                    return true
                }
                // 哔哩哔哩视频处理
                if ("tv.danmaku.bili".checkAppInstalled(this@WebViewActivity) && url.startsWith("bilibili://video")) {
                    MessageDialog.Builder(this@WebViewActivity)
                        .setTitle("提示")
                        .setMessage("是否打开哔哩哔哩客户端看视频")
                        .setConfirm(getString(R.string.common_confirm))
                        .setCancel(getString(R.string.common_cancel))
                        .setListener { url.openBrowser(this@WebViewActivity) }
                        .show()
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

            /** 同名 API 兼容 */
            @TargetApi(Build.VERSION_CODES.M)
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                if (request!!.isForMainFrame) {
                    onReceivedError(view, error!!.errorCode, error.description.toString(), request.url.toString())
                }
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                // 注意一定要去除这行代码，否则设置无效。
                //super.onReceivedSslError(view, handler, error)
                // Android默认的处理方式
                //handler?.cancel()
                // 接受所有网站的证书
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


    /**
     * 打开系统文件选择器
     */
    private fun openSystemFileChooser(
        activity: BaseActivity,
        callback: ValueCallback<Array<Uri?>?>,
        params: FileChooserParams
    ) {
        val intent = params.createIntent()
        val mimeTypes = params.acceptTypes
        if (!mimeTypes.isNullOrEmpty() && mimeTypes[0] != null && "" != mimeTypes[0]) {
            // 设置要过滤的文件类型
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }
        // 设置是否是多选模式
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, params.mode == FileChooserParams.MODE_OPEN_MULTIPLE)
        activity.startActivityForResult(Intent.createChooser(intent, params.title)) { resultCode, data ->
            var uris: Array<Uri?>? = null
            if (resultCode == Activity.RESULT_OK && data != null) {
                val uri = data.data
                if (uri != null) {
                    // 如果用户只选择了一个文件
                    uris = arrayOf(uri)
                } else {
                    // 如果用户选择了多个文件
                    val clipData = data.clipData
                    if (clipData != null) {
                        uris = arrayOfNulls(clipData.itemCount)
                        for (i in 0 until clipData.itemCount) {
                            uris[i] = clipData.getItemAt(i).uri
                        }
                    }
                }
            }
            // 不管用户最后有没有选择文件，最后还是调用 onReceiveValue，如果没有调用就会导致网页再次上传无响应
            callback.onReceiveValue(uris)
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