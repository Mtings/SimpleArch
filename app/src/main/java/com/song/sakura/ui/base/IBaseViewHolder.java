package com.song.sakura.ui.base;

import android.util.DisplayMetrics;
import android.view.View;

public class IBaseViewHolder extends BaseViewHolder {
    protected DisplayMetrics displayMetrics;

    public IBaseViewHolder(View itemView) {
        super(itemView);
        displayMetrics = itemView.getResources().getDisplayMetrics();
    }
}
