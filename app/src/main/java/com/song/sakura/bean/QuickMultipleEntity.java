package com.song.sakura.bean;


import com.chad.library.adapter.base.entity.MultiItemEntity;

public class QuickMultipleEntity implements MultiItemEntity {

    public static final int BANNER = 1;
    public static final int BANNER_AD = 2;
    public static final int MULTI_ICON = 3;

    private int itemType;

    public QuickMultipleEntity(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
