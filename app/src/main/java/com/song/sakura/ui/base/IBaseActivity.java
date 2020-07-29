package com.song.sakura.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.launcher.ARouter;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.network.api.ApiResponse;
import com.song.sakura.BuildConfig;

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
        ARouter.getInstance().inject(this);

        mShareViewModel = getAppViewModelProvider().get(ShareViewModel.class);
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
        ImmersionBar.with(this).init();
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

    @Override
    public void dismissKeyboard() {
        try {
            View view = getWindow().peekDecorView();
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {

        }
    }
}
