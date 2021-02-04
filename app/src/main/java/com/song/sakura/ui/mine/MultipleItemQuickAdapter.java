package com.song.sakura.ui.mine;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.song.sakura.R;
import com.song.sakura.bean.QuickMultipleEntity;

import java.util.List;

public class MultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<QuickMultipleEntity, BaseViewHolder> {

    public MultipleItemQuickAdapter(List<QuickMultipleEntity> data) {
        super(data);
        // 绑定 layout 对应的 type
        addItemType(QuickMultipleEntity.BANNER, R.layout.item_banner);
        addItemType(QuickMultipleEntity.BANNER_AD, R.layout.item_banner_ad);
        addItemType(QuickMultipleEntity.MULTI_ICON, R.layout.item_multi_icon);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, QuickMultipleEntity item) {
        // 根据返回的 type 分别设置数据
        switch (helper.getItemViewType()) {
            case QuickMultipleEntity.BANNER:
                break;
            case QuickMultipleEntity.BANNER_AD:
                break;
            case QuickMultipleEntity.MULTI_ICON:
                break;
            default:
                break;
        }
    }
}
