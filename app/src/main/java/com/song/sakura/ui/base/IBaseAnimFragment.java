package com.song.sakura.ui.base;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.AnimRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.song.sakura.R;

import com.ui.base.BaseViewModel;
import com.ui.base.FragmentBackHelper;

public class IBaseAnimFragment<Q extends BaseViewModel> extends IBaseFragment<Q> implements FragmentBackHelper {

    protected View mContentView;

    private Animation mInAnim;
    private Animation mOutAnim;

    protected @AnimRes
    int inResId = R.anim.translate_in_from_bottom, outResId = R.anim.translate_out_from_top;

    public int getInResId() {
        return inResId;
    }

    public int getOutResId() {
        return outResId;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContentView = view.findViewById(R.id.content);

        mInAnim = AnimationUtils.loadAnimation(getActivity(), getInResId());
        mOutAnim = AnimationUtils.loadAnimation(getActivity(), getOutResId());

        mContentView.setVisibility(View.VISIBLE);
        mContentView.startAnimation(mInAnim);
    }


    @Override
    public boolean onBackPressed() {
        mContentView.setVisibility(View.GONE);
        mContentView.startAnimation(mOutAnim);

        getBaseActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(0, R.anim.alpha_out)
                .remove(this)
                .commitAllowingStateLoss();

        return true;
    }

    protected void contentGone() {
        mContentView.setVisibility(View.GONE);
        mContentView.startAnimation(mOutAnim);
    }

    protected void contentShow() {
        mContentView.setVisibility(View.VISIBLE);
        mContentView.startAnimation(mInAnim);
    }

}
