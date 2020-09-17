package com.ui.base;

import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

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

import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public class BaseActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    protected ViewGroup rootView;
    protected View mProgressView;

    @Nullable
    protected Toolbar mToolbar;
    protected List<FragmentBackHelper> fragmentBackHelperList;
    protected BaseDialog mDialogError;


    public void setFragmentBackHelper(FragmentBackHelper i) {
        if (i != null)
            fragmentBackHelperList.add(i);
    }

    public void removeFragmentBackHelper(FragmentBackHelper i) {
        if (i != null)
            fragmentBackHelperList.remove(i);
    }

    @SuppressWarnings("all")
    public void dismissKeyboard() {
        try {
            this.getCurrentFocus().clearFocus();
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(this.getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            //doNothing
        }

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
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        mToolbar = findViewById(R.id.toolbar);
        setToolbarBackDrawable(mToolbar);
        rootView = (ViewGroup) getWindow().getDecorView();
        initProgressLayout();
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
        if (!TextUtils.isEmpty(error)) {
            dismissErrorDialog();
            mDialogError = new HintDialog.Builder(this)
                    .setIcon(HintDialog.ICON_ERROR)
                    .setCancelable(true)
                    .setMessage(error)
                    .create();
            mDialogError.show();
        }
    }

    @Override
    protected void onDestroy() {
        dismissErrorDialog();
        super.onDestroy();
    }

    public void dismissErrorDialog() {
        if (mDialogError != null && mDialogError.isShowing()) {
            mDialogError.dismiss();
            mDialogError.cancel();
            mDialogError = null;
        }
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
    private void hideSoftKeyboard() {
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
         * @param resultCode        结果码
         * @param data              数据
         */
        void onActivityResult(int resultCode, @Nullable Intent data);
    }

}
