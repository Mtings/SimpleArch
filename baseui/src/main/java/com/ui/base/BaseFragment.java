package com.ui.base;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ui.R;
import com.ui.util.DialogUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.ui.widget.Toolbar;

import java.lang.reflect.Field;

@SuppressWarnings("deprecation")
public class BaseFragment extends Fragment {

    protected RecyclerView.RecycledViewPool mPool;

    public void setPool(RecyclerView.RecycledViewPool pool) {
        this.mPool = pool;
    }

    @Nullable
    protected Toolbar mToolbar;

    protected BaseActivity baseActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            baseActivity = (BaseActivity) context;
        }
        if (this instanceof FragmentBackHelper) {
            getBaseActivity().setFragmentBackHelper((FragmentBackHelper) this);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mToolbar = view.findViewById(R.id.toolbar);
        if (mToolbar == null) {
            mToolbar = getActivity().findViewById(R.id.toolbar);
        }
        if (getActivity().getIntent() != null && getActivity().getIntent().hasExtra(Intent.EXTRA_TITLE)) {
            setTitle(getActivity().getIntent().getStringExtra(Intent.EXTRA_TITLE));
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this instanceof FragmentBackHelper) {
            getBaseActivity().removeFragmentBackHelper((FragmentBackHelper) this);
        }
    }

    /**
     * 解决Fragment嵌套使用时bug
     * 但是在使用NavigationView时会报空指针异常，需注释
     */
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        try {
//            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
//            childFragmentManager.setAccessible(true);
//            childFragmentManager.set(this, null);
//        } catch (NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public void setTitle(@StringRes int resId) {
        if (null != mToolbar)
            mToolbar.setTitle(resId);
    }

    public void setTitle(String resId) {
        if (null != mToolbar)
            mToolbar.setTitle(resId);
    }

    public void setTitleStyle(@StyleRes int resId) {
        if (null != mToolbar)
            mToolbar.setTitleTextAppearance(getContext(), resId);
    }

    public int getColors(@ColorRes int resId) {
        return getResources().getColor(resId);
    }

    protected int getColor(@ColorRes int resId) {
        return getResources().getColor(resId);
    }

    public void setProgressVisible(boolean isVisible) {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        if (baseActivity != null)
            baseActivity.setProgressVisible(isVisible);
    }

    public void setViewDisableDelay(final View view) {
        view.setEnabled(false);
        view.postDelayed(() -> {
            view.setEnabled(true);
        }, 600);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public void startActivity(Intent intent, boolean isBack) {
        super.startActivity(intent);
        if (isBack)
            getActivity().overridePendingTransition(R.anim.left_in, R.anim.right_out);
        else
            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public BaseActivity getBaseActivity() {
        return baseActivity;
    }

    protected void finish() {
        getBaseActivity().finish();
    }

    public void startActivity(Class clz) {
        Intent intent = new Intent(getActivity(), FragmentParentActivity.class);
        intent.putExtra(FragmentParentActivity.KEY_FRAGMENT, clz);
        startActivity(intent);
    }

    public void error(String error) {
        setProgressVisible(false);
        if (!TextUtils.isEmpty(error)) {
            DialogUtil.createDialogView(getActivity(), error, (dialog, which) -> {
                dialog.dismiss();
            }, R.string.btn_confirm);
        }
    }

    public void error(int code, String error) {
        error(error);
    }


    public void dismissKeyboard() {
        getBaseActivity().dismissKeyboard();
    }


}
