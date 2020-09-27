package com.song.sakura.ui.base;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

@SuppressWarnings("unused")
public class BaseViewHolder extends com.chad.library.adapter.base.viewholder.BaseViewHolder {
    protected DisplayMetrics displayMetrics;

    public BaseViewHolder(View itemView) {
        super(itemView);
        displayMetrics = itemView.getResources().getDisplayMetrics();
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int id) {
        return (T) itemView.findViewById(id);
    }

    public <T extends View> T findViewById(@IdRes int resId) {
        return getView(resId);
    }

    public static View inflater(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    public static View inflater(int layoutRes, ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    public Drawable getDrawable(int res) {
        Drawable drawable = ContextCompat.getDrawable(itemView.getContext(), res);
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        }
        return drawable;
    }

    public int getColors(@ColorRes int resId) {
        return ContextCompat.getColor(itemView.getContext(), resId);
    }

    public String getString(@StringRes int resId, String s) {
        return itemView.getContext().getResources().getString(resId) + s;
    }

    public String getString(@StringRes int resId) {
        return itemView.getContext().getResources().getString(resId);
    }

    public void setViewDrawableRight(TextView view, int resId) {
        view.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(resId), null);
    }

    public void setTextViewSize(@IdRes int resId, int size) {
        TextView textView = getView(resId);
        textView.setTextSize(size);
    }

    public void setViewSize(@IdRes int resId, int w, int h) {
        View view = getView(resId);
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = w;
        lp.height = h;
        view.requestLayout();
    }

    public static void setViewSize(View parent, @IdRes int resId, int w, int h) {
        View view = parent.findViewById(resId);
        if (view != null) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.width = w;
            lp.height = h;
            view.requestLayout();
        }
    }

    public void setViewVisible(@IdRes int resId, int visible) {
        View view = getView(resId);
        view.setVisibility(visible);
    }

    public Activity getActivity() {
        return (Activity) itemView.getContext();
    }
}
