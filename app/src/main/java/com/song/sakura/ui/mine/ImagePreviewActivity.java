package com.song.sakura.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.rd.PageIndicatorView;
import com.song.sakura.R;
import com.song.sakura.aop.CheckNet;
import com.song.sakura.ui.base.IBaseActivity;
import com.song.sakura.ui.base.IBaseViewModel;
import com.song.sakura.ui.base.ImagePagerAdapter;
import com.ui.action.BundleAction;
import com.ui.util.IntentBuilder;

import java.util.ArrayList;

public final class ImagePreviewActivity extends IBaseActivity<IBaseViewModel> implements BundleAction {

    public static void start(Context context, String url) {
        ArrayList<String> images = new ArrayList<>(1);
        images.add(url);
        start(context, images);
    }

    public static void start(Context context, ArrayList<String> urls) {
        start(context, urls, 0);
    }

    @CheckNet
    public static void start(Context context, ArrayList<String> urls, int index) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra(IntentBuilder.IMAGE, urls);
        intent.putExtra(IntentBuilder.INDEX, index);
        context.startActivity(intent);
    }

    private ViewPager mViewPager;
    private PageIndicatorView mIndicatorView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImmersionBar();
        setContentView(R.layout.activity_image_preview);
        initView();
        initData();

    }


    private void initView() {
        mViewPager = findViewById(R.id.vp_image_preview_pager);
        mIndicatorView = findViewById(R.id.pv_image_preview_indicator);
        mIndicatorView.setViewPager(mViewPager);
    }

    private void initData() {
        ArrayList<String> images = getStringArrayList(IntentBuilder.IMAGE);
        int index = getInt(IntentBuilder.INDEX);
        if (images != null && images.size() > 0) {
            mViewPager.setAdapter(new ImagePagerAdapter(this, images));
            if (index != 0 && index <= images.size()) {
                mViewPager.setCurrentItem(index);
            }
        } else {
            finish();
        }
    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this).statusBarDarkFont(false).hideBar(BarHide.FLAG_HIDE_BAR).init();
    }

    @Nullable
    @Override
    public Bundle getBundle() {
        return getIntent().getExtras();
    }
}