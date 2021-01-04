package com.song.sakura.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.alibaba.android.arouter.launcher.ARouter
import com.hjq.toast.ToastInterceptor
import com.hjq.toast.ToastUtils
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.song.sakura.BuildConfig
import com.song.sakura.R
import com.song.sakura.database.WordRepository
import com.song.sakura.database.WordRoomDatabase
import com.song.sakura.helper.ActivityStackManager
import com.ui.util.LogUtil

class App : Application(), ViewModelStoreOwner, LifecycleOwner {

    companion object {
        lateinit var mApplication: Application

        fun getApplication(): Context {
            return mApplication
        }

        fun isNetworkConnected(): Boolean {
            val mConnectivityManager = getApplication()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            return mNetworkInfo?.isAvailable ?: false
        }

        // Using by lazy so the database and the repository are only created when they're needed
        // rather than when the application starts
        val database by lazy { WordRoomDatabase.getDatabase(getApplication()) }
        val repository by lazy { WordRepository(database.wordDao()) }
    }

    init {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context: Context?, refreshLayout: RefreshLayout ->
            //设置内容不偏移
            refreshLayout.setEnableHeaderTranslationContent(false)
            MaterialHeader(context)
                .setProgressBackgroundColorSchemeResource(R.color.colorAccent)
                .setColorSchemeResources(R.color.white, R.color.white, R.color.white)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context: Context?, refreshLayout: RefreshLayout ->
            //设置内容不偏移
            refreshLayout.setEnableFooterTranslationContent(false)
            BallPulseFooter(context)
                .setNormalColor(ContextCompat.getColor(getApplication(), R.color.colorAccent))
                .setAnimatingColor(ContextCompat.getColor(getApplication(), R.color.colorAccent))
        }
    }

    private lateinit var mAppViewModelStore: ViewModelStore
    private var mFactory: ViewModelProvider.Factory? = null
    private val mLifecycle = LifecycleRegistry(this)

    override fun onCreate() {
        super.onCreate()

        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        mApplication = this
        mAppViewModelStore = ViewModelStore()

        if (BuildConfig.DEBUG) {
            LogUtil.DEBUG = true
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(this)

        // 吐司工具类
        ToastUtils.init(this)

        // 设置 Toast 拦截器
        ToastUtils.setToastInterceptor(object : ToastInterceptor() {
            override fun intercept(toast: Toast, text: CharSequence): Boolean {
                val intercept = super.intercept(toast, text)
                if (intercept) Log.e("Toast", "空 Toast") else Log.i("Toast", text.toString())
                return intercept
            }
        })

        // Activity 栈管理初始化
        ActivityStackManager.getInstance().init(this)
    }


    override fun getLifecycle(): Lifecycle {
        return mLifecycle
    }

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }

    fun getAppViewModelProvider(activity: Activity): ViewModelProvider {
        return ViewModelProvider(activity.applicationContext as App, getAppFactory(activity)!!)
    }

    private fun getAppFactory(activity: Activity): ViewModelProvider.Factory? {
        val application = checkApplication(activity)
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        }
        return mFactory
    }

    private fun checkApplication(activity: Activity): Application {
        return activity.application
            ?: throw IllegalStateException(
                "Your activity/fragment is not yet attached to "
                        + "Application. You can't request ViewModel before onCreate call."
            )
    }
}

