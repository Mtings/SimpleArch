package com.song.sakura.ui.launch;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.gyf.immersionbar.ImmersionBar;
import com.song.sakura.R;
import com.song.sakura.ui.base.IBaseActivity;
import com.song.sakura.ui.base.IBaseViewModel;
import com.song.sakura.ui.main.MainActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LaunchActivity extends IBaseActivity<IBaseViewModel> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_launch);
        super.onCreate(savedInstanceState);
        initImmersionBar();
        if (!isTaskRoot()) {
            finish();
            return;
        }

        Observable.timer(2, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long aLong) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                MainActivity.Companion.goMain(LaunchActivity.this);
            }
        });

    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .transparentStatusBar()
                .statusBarDarkFont(false).init();
    }

    @Override
    public void onBackPressed() {
        //禁用onBackPressed()
    }
}
