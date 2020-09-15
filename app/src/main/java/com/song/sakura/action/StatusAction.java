package com.song.sakura.action;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.RawRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.song.sakura.R;
import com.song.sakura.widget.HintLayout;

public interface StatusAction {

    /**
     * 获取提示布局
     */
    HintLayout getHintLayout();

    /**
     * 显示加载中
     */
    default void showLoading() {
        showLoading(R.raw.dolphin_jump);
    }

    default void showLoading(@RawRes int id) {
        HintLayout layout = getHintLayout();
        layout.show();
        layout.setAnim(id);
        layout.setHint("");
        layout.setOnClickListener(null);
    }

    /**
     * 显示加载完成
     */
    default void showComplete() {
        HintLayout layout = getHintLayout();
        if (layout != null && layout.isShow()) {
            layout.hide();
        }
    }

    /**
     * 显示空提示
     */
    default void showEmpty() {
        showLayout(R.drawable.ic_warning, R.string.hint_layout_no_data, null);
    }

    /**
     * 显示错误提示
     */
    default void showError(View.OnClickListener listener) {
        HintLayout layout = getHintLayout();
        Context context = layout.getContext();
        ConnectivityManager manager = ContextCompat.getSystemService(context, ConnectivityManager.class);
        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            // 判断网络是否连接
            if (info == null || !info.isConnected()) {
                showLayout(R.drawable.ic_nerwork_hint, R.string.hint_layout_error_network, listener);
                return;
            }
        }
        showLayout(R.drawable.ic_error, R.string.hint_layout_error_request, listener);
    }

    /**
     * 显示自定义提示
     */
    default void showLayout(@DrawableRes int drawableId, @StringRes int stringId, View.OnClickListener listener) {
        HintLayout layout = getHintLayout();
        Context context = layout.getContext();
        showLayout(ContextCompat.getDrawable(context, drawableId), context.getString(stringId), listener);
    }

    default void showLayout(Drawable drawable, CharSequence hint, View.OnClickListener listener) {
        HintLayout layout = getHintLayout();
        layout.show();
        layout.setIcon(drawable);
        layout.setHint(hint);
        layout.setOnClickListener(listener);
    }
}