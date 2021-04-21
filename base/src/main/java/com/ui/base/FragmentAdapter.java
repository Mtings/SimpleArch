package com.ui.base;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FragmentAdapter extends FragmentStatePagerAdapter {
    RecyclerView.RecycledViewPool mPool;
    private List<Fragment> mFragments;
    private List<String> mTitles;

    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mFragments = fragments;
        mTitles = titles;
        mPool = new RecyclerView.RecycledViewPool();
    }

    @NonNull
    public List<Fragment> getFragments() {
        return mFragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (mFragments.get(position) instanceof BaseFragment) {
            BaseFragment fragment = (BaseFragment) mFragments.get(position);
            fragment.setPool(mPool);
            return fragment;
        } else {
            return mFragments.get(position);
        }
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
