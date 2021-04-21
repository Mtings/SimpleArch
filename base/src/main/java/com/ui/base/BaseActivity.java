package com.ui.base;

import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.ui.action.HandlerAction;
import com.ui.util.Lists;

import com.ui.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.snackbar.Snackbar;
import com.ui.widget.Toolbar;
import com.ui.widget.view.SmartTextView;

import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public class BaseActivity extends AppCompatActivity implements HandlerAction {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    protected ViewGroup rootView;
    protected View mProgressView;
    private View finishView;
    private View errorView;
    private SmartTextView hintMsg;
    private SmartTextView errorMsg;

    @Nullable
    protected Toolbar mToolbar;
    protected List<FragmentBackHelper> fragmentBackHelperList;

    public void setFragmentBackHelper(FragmentBackHelper i) {
        if (i != null)
            fragmentBackHelperList.add(i);
    }

    public void removeFragmentBackHelper(FragmentBackHelper i) {
        if (i != null)
            fragmentBackHelperList.remove(i);
    }

    public void setRootView(ViewGroup rootView) {
        this.rootView = rootView;
    }

    public void setViewDisableDelay(final View view) {
        view.setEnabled(false);
        view.postDelayed(() -> view.setEnabled(true), 600);
    }

    protected void initProgressLayout() {
        if (mProgressView == null) {
            mProgressView = getLayoutInflater().inflate(R.layout.loading_layout, rootView
                    , false);
            mProgressView.setOnClickListener(v -> {
            });
            setProgressVisible(false);
            rootView.addView(mProgressView);
        }
    }

    private void initHintLayout() {
        if (finishView == null) {
            finishView = getLayoutInflater().inflate(R.layout.dialog_finish, rootView
                    , false);
            hintMsg = finishView.findViewById(R.id.hintMsg);
            setFinishViewVisible(false);
            rootView.addView(finishView);
        }
        if (errorView == null) {
            errorView = getLayoutInflater().inflate(R.layout.dialog_error, rootView
                    , false);
            errorMsg = errorView.findViewById(R.id.errorMsg);
            setErrorViewVisible(false);
            rootView.addView(errorView);
        }
    }

    public void setFinishViewVisible(boolean show) {
        if (finishView != null) {
            finishView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public void setErrorViewVisible(boolean show) {
        if (errorView != null) {
            errorView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public void setProgressVisible(boolean show) {
        if (mProgressView != null) {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public int getColors(@ColorRes int resId) {
        return getResources().getColor(resId);
    }

    @Override
    public void onBackPressed() {
        for (int i = fragmentBackHelperList.size() - 1; i > -1; i--) {
            if (fragmentBackHelperList.get(i).onBackPressed()) return;
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mToolbar = findViewById(R.id.toolbar);
        setToolbarBackDrawable(mToolbar);
        rootView = (ViewGroup) getWindow().getDecorView();
        initProgressLayout();
        initHintLayout();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        mToolbar = findViewById(R.id.toolbar);
        setToolbarBackDrawable(mToolbar);
        rootView = (ViewGroup) getWindow().getDecorView();
        initProgressLayout();
        initHintLayout();
    }


    public void setToolbarBackDrawable(Toolbar mToolbar) {
        if (null != mToolbar) {
            mToolbar.setNavigationOnClickListener(e -> onBackPressed());
        }
    }

    public void showSnackBar(@StringRes int stringRes) {
        Snackbar.make(getWindow().getDecorView(), stringRes, Snackbar.LENGTH_LONG).show();
    }

    public void showSnackBar(View view, @StringRes int stringRes) {
        Snackbar.make(view == null ? getWindow().getDecorView() : view, stringRes, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        fragmentBackHelperList = Lists.newArrayList();
        super.onCreate(savedInstanceState);
    }

    public void error(String error) {
        setProgressVisible(false);
        setFinishViewVisible(false);
        if (!TextUtils.isEmpty(error)) {
            errorMsg.setText(error);
            setErrorViewVisible(true);
            postDelayed(() -> {
                setErrorViewVisible(false);
                errorMsg.setText("");
            }, 1500);
        }
    }

    public void complete(String msg) {
        setProgressVisible(false);
        setErrorViewVisible(false);
        if (!TextUtils.isEmpty(msg)) {
            hintMsg.setText(msg);
            setFinishViewVisible(true);
            postDelayed(() -> {
                setFinishViewVisible(false);
                hintMsg.setText("");
            }, 1500);
        }
    }

    @Override
    protected void onDestroy() {
        removeCallbacks();
        super.onDestroy();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

    }

    public Activity getActivity() {
        return this;
    }


    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard() {
        // 隐藏软键盘，避免软键盘引发的内存泄露
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null && manager.isActive(view)) {
                manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * startActivityForResult 方法优化
     */

    private OnActivityCallback mActivityCallback;
    private int mActivityRequestCode;

    public void startActivityForResult(Class<? extends Activity> clazz, OnActivityCallback callback) {
        startActivityForResult(new Intent(this, clazz), null, callback);
    }

    public void startActivityForResult(Intent intent, OnActivityCallback callback) {
        startActivityForResult(intent, null, callback);
    }

    public void startActivityForResult(Intent intent, @Nullable Bundle options, OnActivityCallback callback) {
        // 回调还没有结束，所以不能再次调用此方法，这个方法只适合一对一回调，其他需求请使用原生的方法实现
        if (mActivityCallback == null) {
            mActivityCallback = callback;
            // 随机生成请求码，这个请求码必须在 2 的 16 次幂以内，也就是 0 - 65535
            mActivityRequestCode = new Random().nextInt((int) Math.pow(2, 16));
            startActivityForResult(intent, mActivityRequestCode, options);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (mActivityCallback != null && mActivityRequestCode == requestCode) {
            mActivityCallback.onActivityResult(resultCode, data);
            mActivityCallback = null;
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        hideSoftKeyboard();
        // 查看源码得知 startActivity 最终也会调用 startActivityForResult
        super.startActivityForResult(intent, requestCode, options);
    }

    public interface OnActivityCallback {

        /**
         * 结果回调
         *
         * @param resultCode 结果码
         * @param data       数据
         */
        void onActivityResult(int resultCode, @Nullable Intent data);
    }

}
