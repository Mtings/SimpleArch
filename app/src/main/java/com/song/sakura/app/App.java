package com.song.sakura.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hjq.toast.ToastInterceptor;
import com.hjq.toast.ToastUtils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.song.sakura.BuildConfig;

import com.song.sakura.R;
import com.song.sakura.helper.ActivityStackManager;
import com.ui.util.DrawableHelper;
import com.ui.util.LogUtil;

public class App extends Application implements ViewModelStoreOwner {

    private static Application mApplication;

    private ViewModelStore mAppViewModelStore;
    private ViewModelProvider.Factory mFactory;

    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, refreshLayout) -> {
            //设置内容不偏移
            refreshLayout.setEnableHeaderTranslationContent(false);
            return new MaterialHeader(context)
                    .setProgressBackgroundColorSchemeResource(R.color.colorAccent)
                    .setColorSchemeResources(R.color.white, R.color.white, R.color.white);
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, refreshLayout) -> new ClassicsFooter(context).setDrawableSize(20));
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mApplication = this;
        mAppViewModelStore = new ViewModelStore();

        if (BuildConfig.DEBUG) {
            LogUtil.DEBUG = true;
            ARouter.openDebug();
            ARouter.openLog();
        }
        ARouter.init(this);

        DrawableHelper.init(this);
        // 吐司工具类
        ToastUtils.init(this);

        // 设置 Toast 拦截器
        ToastUtils.setToastInterceptor(new ToastInterceptor() {
            @Override
            public boolean intercept(Toast toast, CharSequence text) {
                boolean intercept = super.intercept(toast, text);
                if (intercept) {
                    Log.e("Toast", "空 Toast");
                } else {
                    Log.i("Toast", text.toString());
                }
                return intercept;
            }
        });

        // Activity 栈管理初始化
        ActivityStackManager.getInstance().init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(base);
    }

    public static Context getApplication() {
        return mApplication;
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return mAppViewModelStore;
    }

    public ViewModelProvider getAppViewModelProvider(Activity activity) {
        return new ViewModelProvider((App) activity.getApplicationContext(),
                ((App) activity.getApplicationContext()).getAppFactory(activity));
    }

    private ViewModelProvider.Factory getAppFactory(Activity activity) {
        Application application = checkApplication(activity);
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application);
        }
        return mFactory;
    }

    private Application checkApplication(Activity activity) {
        Application application = activity.getApplication();
        if (application == null) {
            throw new IllegalStateException("Your activity/fragment is not yet attached to "
                    + "Application. You can't request ViewModel before onCreate call.");
        }
        return application;
    }

    private Activity checkActivity(Fragment fragment) {
        Activity activity = fragment.getActivity();
        if (activity == null) {
            throw new IllegalStateException("Can't create ViewModelProvider for detached fragment");
        }
        return activity;
    }

    public static Boolean isNetworkConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) App.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

}
