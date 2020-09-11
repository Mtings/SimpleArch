package com.ui.base;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
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

import com.ui.widget.Toolbar;

@SuppressWarnings("unused")
public class BaseFragment extends Fragment {

    protected RecyclerView.RecycledViewPool mPool;

    public void setPool(RecyclerView.RecycledViewPool pool) {
        this.mPool = pool;
    }

    @Nullable
    protected Toolbar mToolbar;

    protected BaseActivity baseActivity;

    @Override
    public void onAttach(@NonNull Context context) {
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
            mToolbar = requireActivity().findViewById(R.id.toolbar);
        }
        if (requireActivity().getIntent() != null && requireActivity().getIntent().hasExtra(Intent.EXTRA_TITLE)) {
            setTitle(requireActivity().getIntent().getStringExtra(Intent.EXTRA_TITLE));
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this instanceof FragmentBackHelper) {
            getBaseActivity().removeFragmentBackHelper((FragmentBackHelper) this);
        }
    }

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
        view.postDelayed(() -> view.setEnabled(true), 600);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public void startActivity(Intent intent, boolean isBack) {
        super.startActivity(intent);
        if (isBack)
            requireActivity().overridePendingTransition(R.anim.left_in, R.anim.right_out);
        else
            requireActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public BaseActivity getBaseActivity() {
        return baseActivity;
    }

    protected void finish() {
        getBaseActivity().finish();
    }

    public void startActivity(Class<?> clz) {
        Intent intent = new Intent(getActivity(), FragmentParentActivity.class);
        intent.putExtra(FragmentParentActivity.KEY_FRAGMENT, clz);
        startActivity(intent);
    }

    public void error(String error) {
        setProgressVisible(false);
        if (!TextUtils.isEmpty(error)) {
            DialogUtil.createDialogView(getActivity(), error, (dialog, which) -> dialog.dismiss(), R.string.btn_confirm);
        }
    }

    public void error(int code, String error) {
        error(error);
    }


    public void dismissKeyboard() {
        getBaseActivity().dismissKeyboard();
    }


}
