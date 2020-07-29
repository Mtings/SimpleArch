package com.ui.base;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;

public abstract class BaseLazyFragment<Q extends BaseViewModel> extends BaseLiveDataFragment<Q>{
    protected boolean isVisible;
    protected boolean isPrepared;   // 标志位，标志已经初始化完成。
    /**
     * 是否已被加载过一次，第二次就不再去请求数据
     */
    private boolean mHasLoaded;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInVisible();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepared = true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onVisible();
    }

    protected boolean isLazyLoad() {
        return !(!isPrepared || !isVisible || mHasLoaded);
    }

    protected void onVisible() {
        if (isLazyLoad())lazyLoad();
    }

    protected void onInVisible() {

    }

    public abstract void lazyLoad();

    public boolean isHasLoaded() {
        return mHasLoaded;
    }

    public void setHasLoaded(boolean hasLoaded) {
        this.mHasLoaded = hasLoaded;
    }
}
