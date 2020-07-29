package com.song.sakura.ui.base;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.song.sakura.R;

import com.ui.base.BaseViewModel;
import com.ui.widget.Toolbar;

public abstract class IBaseToolbarFragment<Q extends BaseViewModel> extends IBaseFragment<Q> {

    protected FrameLayout mFrameLayout;

    protected abstract @LayoutRes
    int getLayoutResId();

    protected abstract View getLayoutView();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_with_toolbar_layout, container, false);
        mFrameLayout = view.findViewById(R.id.frame_holder);
        mFrameLayout.setBackgroundColor(Color.WHITE);
        if (getLayoutView() != null) {
            mFrameLayout.addView(getLayoutView());
        } else {
            inflater.inflate(getLayoutResId(), mFrameLayout);
        }
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view1 -> {
            baseActivity.onBackPressed();
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
