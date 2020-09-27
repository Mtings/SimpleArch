package com.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.*;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.content.ContextCompat;

import com.ui.util.Utils;

@SuppressLint("RestrictedApi")
public class Toolbar extends androidx.appcompat.widget.Toolbar {
    TextView mTitleText;

    public Toolbar(Context context) {
        super(context);
    }

    @SuppressLint("PrivateResource")
    public Toolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                androidx.appcompat.R.styleable.Toolbar);

        int mTitleTextAppearance = a.getResourceId(androidx.appcompat.R.styleable.Toolbar_titleTextAppearance, 0);
        if (mTitleTextAppearance != 0)
            setTitleTextAppearance(context, mTitleTextAppearance);
        final CharSequence title = a.getText(androidx.appcompat.R.styleable.Toolbar_title);
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }
        if (a.hasValue(androidx.appcompat.R.styleable.Toolbar_titleTextColor)) {
            setTitleTextColor(a.getColor(androidx.appcompat.R.styleable.Toolbar_titleTextColor, 0xffffffff));
        }
    }

    @SuppressLint("PrivateResource")
    public Toolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                androidx.appcompat.R.styleable.Toolbar, defStyleAttr, 0);

        int mTitleTextAppearance = a.getResourceId(androidx.appcompat.R.styleable.Toolbar_titleTextAppearance, 0);
        if (mTitleTextAppearance != 0)
            setTitleTextAppearance(context, mTitleTextAppearance);
        final CharSequence title = a.getText(androidx.appcompat.R.styleable.Toolbar_title);
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }
        if (a.hasValue(androidx.appcompat.R.styleable.Toolbar_titleTextColor)) {
            setTitleTextColor(a.getColor(androidx.appcompat.R.styleable.Toolbar_titleTextColor, 0xffffffff));
        }
    }

    public void setTextDrawableLeft(@DrawableRes int resId) {
        if (mTitleText != null) {
            mTitleText.setCompoundDrawablePadding(Utils.dip2px(getContext(), 10));
            mTitleText.setCompoundDrawables(
                    ContextCompat.getDrawable(getContext(),resId),
                    null, null, null
            );
        }
    }

    public void clearMenu() {
        getMenu().clear();
    }

    /**
     * 添加toolbar右侧文字时可用
     */
    public void addTextRight(String text) {
        getMenu().add(0, 0, 0, text).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    /**
     * 添加toolbar右侧图片时可用
     */
    public void addRightMenu(@MenuRes int resId) {
        inflateMenu(resId);
    }

    @Override
    public void setTitleTextAppearance(Context context, @StyleRes int resId) {
        if (mTitleText != null) {
            mTitleText.setTextAppearance(context, resId);
        } else
            super.setTitleTextAppearance(context, resId);
    }

    @Override
    public void setTitleTextColor(@ColorInt int color) {

        if (mTitleText != null) {
            mTitleText.setTextColor(color);
        } else super.setTitleTextColor(color);
    }

    @Override
    public void setTitle(@StringRes int resId) {
        if (mTitleText != null)
            mTitleText.setText(resId);
        else
            super.setTitle(resId);
    }

    @Override
    public void setTitle(CharSequence title) {
        if (mTitleText != null)
            mTitleText.setText(title);
        else
            super.setTitle(title);
    }

    private void init() {
        mTitleText = new TextView(getContext());
        LayoutParams lp = new LayoutParams(Utils.dip2px(getContext(), ViewGroup.LayoutParams.WRAP_CONTENT),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        mTitleText.setMaxWidth(getResources().getDisplayMetrics().widthPixels - Utils.dip2px(getContext(), 120));
        mTitleText.setGravity(Gravity.CENTER);
        mTitleText.setLayoutParams(lp);
        mTitleText.setSingleLine(true);
        mTitleText.setMaxLines(1);
        mTitleText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mTitleText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        addView(mTitleText);
    }
}
