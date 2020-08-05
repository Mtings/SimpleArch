package com.ui.base;

import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;
import com.ui.util.DrawableHelper;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    protected DisplayMetrics displayMetrics;

    public BaseViewHolder(View itemView) {
        super(itemView);
        displayMetrics = itemView.getResources().getDisplayMetrics();
    }

    /**
     * an exception if the view doesn't exist.
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int id) {
        T result = (T) itemView.findViewById(id);
        if (result == null) {
            return null;
        }
        return result;
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

        Drawable drawable = DrawableHelper.getDrawable(itemView.getContext(), res);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    public int getColors(@ColorRes int resId) {
        return itemView.getContext().getResources().getColor(resId);
    }

    public String getString(@StringRes int resId, String s) {
        return itemView.getContext().getResources().getString(resId)+s;
    }

    public String getString(@StringRes int resId) {
        return itemView.getContext().getResources().getString(resId);
    }
    public void setViewDrawableRight(TextView view, int resId) {
        view.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null
                , getDrawable(resId), null);

    }

    public void setTextView(TextView textView, CharSequence... text) {
        if (textView == null) return;
        CharSequence t = getArrayString(text);
        if (TextUtils.isEmpty(t)) textView.setText("");
        else textView.setText(t);
        textView.setVisibility(View.VISIBLE);
    }

    public void setTextView(@IdRes int resId, CharSequence... text) {
        TextView textView = getView(resId);
        setTextView(textView, text);
    }

    public void setTextViewSize(@IdRes int resId, int size) {
        TextView textView = getView(resId);
        textView.setTextSize(size);
    }

    private CharSequence getArrayString(CharSequence... text) {
        String s = "";
        StringBuilder str = new StringBuilder();
        if (text == null || text.length == 0) {
            return str;
        }
        boolean isImages = false;
        for (CharSequence img : text) {
            str.append(img);
            str.append(s);
            isImages = true;
        }
        if (isImages && s.length() > 0) {
           return str.substring(0, str.length() - s.length());
        }
        return str;
    }

//    public void setIconView(@IdRes int resId, String url) {
//        CustomDraweeView icon = getView(resId);
//        if (icon != null) {
//            BaseImageLoadUtil.Builder().load(url)
//                    .build().imageOptions(R.color.color_999999)
//                    .displayImage(icon);
//        }
//    }

    public void setViewSize(@IdRes int resId, int w, int h) {
        View view = getView(resId);
        if (view != null) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.width = w;
            lp.height = h;
            view.requestLayout();
        }
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
        if (view != null) {
            view.setVisibility(visible);
        }
    }

   public Activity getActivity(){
        return (Activity) itemView.getContext();
    }

}
