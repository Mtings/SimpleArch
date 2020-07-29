package com.song.sakura.ui.base;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toolbar;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.song.sakura.R;

import com.ui.base.BaseViewModel;

public abstract class BaseToolbarFragment<Q extends BaseViewModel> extends IBaseFragment<Q> {

    protected FrameLayout mFrameLayout;

    protected abstract @LayoutRes
    int getLayoutResId();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_with_toolbar_layout, container, false);
        mFrameLayout = view.findViewById(R.id.frame_holder);
        mFrameLayout.setBackgroundColor(Color.WHITE);
        if (getResources().getLayout(getLayoutResId()) != null) {
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
}
