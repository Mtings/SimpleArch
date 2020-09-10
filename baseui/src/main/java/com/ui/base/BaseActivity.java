package com.ui.base;

import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.ui.util.Lists;

import com.ui.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class BaseActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    protected ViewGroup rootView;
    protected View mProgressView;

    protected AppBarLayout mAppBarLayout;
    @Nullable
    protected Toolbar mToolbar;
    protected List<FragmentBackHelper> fragmentBackHelperList;
    protected Dialog mDialogError;


    public void setFragmentBackHelper(FragmentBackHelper i) {
        if (i != null)
            fragmentBackHelperList.add(i);
    }


    public void removeFragmentBackHelper(FragmentBackHelper i) {
        if (i != null && fragmentBackHelperList.contains(i))
            fragmentBackHelperList.remove(i);
    }

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
        view.postDelayed(() -> {
            view.setEnabled(true);
        }, 600);
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
            if (mDialogError != null) {
                mDialogError.dismiss();
                mDialogError = null;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(error);
            builder.setPositiveButton(R.string.btn_confirm, (dialog, which) -> {
                dialog.dismiss();
            });
            mDialogError = builder.show();
        }
    }

    @Override
    protected void onDestroy() {
        if (mDialogError != null && mDialogError.isShowing()) {
            mDialogError.dismiss();
        }
        super.onDestroy();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

    }

    public Activity getActivity() {
        return this;
    }

}
