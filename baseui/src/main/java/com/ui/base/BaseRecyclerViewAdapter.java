package com.ui.base;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;
import com.ui.util.Lists;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("deprecation")
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    public static final int TYPE_HEADER = 1;
    protected static final int TYPE_LINE = 2;
    public static final int TYPE_FOOTER = 3;

    protected ArrayList<T> mList;
    private BaseFragment fragment;
    Context mContext ;

    public void setFragment(BaseFragment fragment) {
        this.fragment = fragment;
    }

    public BaseFragment getFragment() {
        return fragment;
    }

    protected int getColor(@ColorRes int resId) {
        return mContext.getResources().getColor(resId);
    }

    protected String getString(@StringRes int resId) {
        return mContext.getResources().getString(resId);
    }

    protected String getString(@StringRes int resId, Object... formatArgs) {
        return mContext.getResources().getString(resId, formatArgs);
    }

    protected BaseRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    protected BaseRecyclerViewAdapter() {

    }

    public void removeItem(int position) {
        this.mList.remove(position);
        this.notifyDataSetChanged();
    }

    public void removeItem(T obj) {
        this.mList.remove(obj);
        this.notifyDataSetChanged();
    }

    public void clearAll() {
        if (mList != null) {
            mList.clear();
            notifyDataSetChanged();
        }
    }


    public ArrayList<T> getList() {
        return mList;
    }

    public void setList(T[] list) {
        setList(Arrays.asList(list));
    }

    public void setList(List<T> list) {
        if (list != null) {
            ArrayList<T> arrayList = new ArrayList<T>(list.size());
            arrayList.addAll(list);
            this.mList = arrayList;
        } else {
            this.mList = Lists.newArrayList();
        }
        notifyDataSetChanged();
    }

    public void addList(List<T> list) {
        if (this.mList == null) {
            ArrayList<T> arrayList = new ArrayList<T>(list.size());
            arrayList.addAll(list);
            this.mList = arrayList;
        } else this.mList.addAll(list);
        notifyDataSetChanged();
    }


    public T getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public int getItemCount() {
        return getAdapterItemCount();
    }


    public int getAdapterItemCount() {
        return mList == null ? 0 : mList.size();
    }



    public View inflater(int layoutRes, ViewGroup parent) {
        mContext = parent.getContext();
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    public BaseActivity getActivity(){
        return (BaseActivity) mContext;
    }

    public Context getContext() {
        return mContext;
    }

    protected Animator[] getAnimators(View view) {
        return new Animator[]{ObjectAnimator.ofFloat(view, "translationX", (float) view.getRootView().getWidth(), 0.0F)};
    }

}
