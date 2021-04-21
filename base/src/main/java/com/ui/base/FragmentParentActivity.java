package com.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ui.R;

public class FragmentParentActivity extends BaseActivity {

    public static void startActivity(Activity context, Class<?> clz, boolean isToolbar) {
        Intent intent = new Intent(context, FragmentParentActivity.class);
        intent.putExtra(FragmentParentActivity.KEY_FRAGMENT, clz);
        intent.putExtra(FragmentParentActivity.KEY_TOOLBAR, isToolbar);
        context.startActivity(intent);
    }

    public static void startActivity(Activity context, Class<?> clz) {
        Intent intent = new Intent(context, FragmentParentActivity.class);
        intent.putExtra(FragmentParentActivity.KEY_FRAGMENT, clz);
        context.startActivity(intent);
    }

    public final static String KEY_FRAGMENT_NAME = "KEY_FRAGMENT_NAME";

    public final static String KEY_FRAGMENT = "KEY_FRAGMENT";
    public final static String KEY_TOOLBAR = "KEY_TOOLBAR";
    private BaseFragment baseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isToolbar = getIntent().getBooleanExtra(KEY_TOOLBAR, true);
        Class<?> clz = (Class<?>) getIntent().getSerializableExtra(KEY_FRAGMENT);
        String cls;
        if (clz != null) {
            cls = clz.getName();
        } else {
            cls = getIntent().getStringExtra(KEY_FRAGMENT_NAME);
        }

        if (isToolbar) {
            setContentView(R.layout.activity_with_toolbar_layout);
            Fragment fragment = Fragment.instantiate(getActivity(), cls);
            if (fragment instanceof BaseFragment)
                baseFragment = (BaseFragment) fragment;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_holder, fragment, cls);
            ft.commitAllowingStateLoss();
        } else {
            setRootView((ViewGroup) getWindow().getDecorView());
            initProgressLayout();
            Fragment fragment = Fragment.instantiate(getActivity(), cls);
            if (fragment instanceof BaseFragment)
                baseFragment = (BaseFragment) fragment;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(android.R.id.content, fragment, cls);
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (baseFragment != null)
            baseFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
