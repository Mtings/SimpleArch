package com.song.sakura.ui.base;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.launcher.ARouter;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.network.api.ApiResponse;
import com.song.sakura.BuildConfig;

import com.song.sakura.R;
import com.song.sakura.app.App;
import com.song.sakura.ui.main.ShareViewModel;
import com.ui.base.BaseActivity;
import com.ui.base.BaseViewModel;
import com.ui.util.LogUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class IBaseActivity<Q extends BaseViewModel> extends BaseActivity {

    public final CompositeDisposable subscription = new CompositeDisposable();

    protected Q mViewModel;
    protected ShareViewModel mShareViewModel;

    public static <B> Boolean isSuccess(ApiResponse<B> it) {
        return null != it && it.getErrorCode() == 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomDensity(this, App.Companion.getMApplication(), 375f);
        ARouter.getInstance().inject(this);

        mShareViewModel = getAppViewModelProvider().get(ShareViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCustomDensity(this, App.Companion.getMApplication(), 375f);
    }

    public void initViewModel(AppCompatActivity activity, Class<Q> modelClass) {
        this.mViewModel = getActivityViewModelProvider(activity).get(modelClass);
    }

    protected ViewModelProvider getAppViewModelProvider() {
        return ((App) getApplicationContext()).getAppViewModelProvider(this);
    }

    protected ViewModelProvider getActivityViewModelProvider(AppCompatActivity activity) {
        return new ViewModelProvider(activity, activity.getDefaultViewModelProviderFactory());
    }

    public ShareViewModel getSharedViewModel() {
        return mShareViewModel;
    }

    public void initImmersionBar() {
        ImmersionBar.with(this).keyboardEnable(true)
                .statusBarDarkFont(true)
                .titleBar(R.id.appbar)
                .init();
    }


    public boolean isImmersionBarEnabled() {
        return false;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.dispose();
    }

    public <T> void bindUi(Observable<T> observable, Consumer<? super T> onNext, Consumer<Throwable> onError) {
        subscription.add(observable.observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError));
    }

    public <T> void bindUi(Observable<T> observable, Consumer<? super T> onNext) {
        subscription.add(observable.observeOn(AndroidSchedulers.mainThread()).subscribe(onNext,
                throwable -> {
                    if (BuildConfig.DEBUG) {
                        ToastUtils.show(throwable.getMessage());
                        LogUtil.print(throwable);
                    }
                }
        ));
    }

    private static float sNoncompatDensity;
    private static float sNoncompatScaledDensity;

    /**
     * 今日头条屏幕适配
     * https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA
     *
     * @param widthDp ui设置设计图宽: ep: 360dp/720dp
     */
    private static void setCustomDensity(@NonNull Activity activity, @NonNull Application application, float widthDp) {
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();

        if (sNoncompatDensity == 0) {
            sNoncompatDensity = appDisplayMetrics.density;
            sNoncompatScaledDensity = appDisplayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(@NonNull Configuration newConfig) {
                    if (newConfig.fontScale > 0) {
                        sNoncompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }

        final float targetDensity = appDisplayMetrics.widthPixels / widthDp;
        final float targetScaledDensity = targetDensity * (sNoncompatScaledDensity / sNoncompatDensity);
        final int targetDensityDpi = (int) (160 * targetDensity);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }

}
