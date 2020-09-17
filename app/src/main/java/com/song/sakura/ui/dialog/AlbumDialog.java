package com.song.sakura.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.song.sakura.R;
import com.song.sakura.ui.base.BaseViewHolder;
import com.ui.base.BaseDialog;

import java.util.List;

public final class AlbumDialog {

    public static final class Builder
            extends BaseDialog.Builder<Builder> implements OnItemClickListener {

        private OnListener mListener;

        private final RecyclerView mRecyclerView;
        private final AlbumAdapter mAdapter;

        public Builder(Context context) {
            super(context);

            setContentView(R.layout.dialog_album);
            setHeight(getResources().getDisplayMetrics().heightPixels / 2);

            mRecyclerView = findViewById(R.id.rv_album_list);
            mAdapter = new AlbumAdapter();
            mAdapter.setOnItemClickListener(this);
            mRecyclerView.setAdapter(mAdapter);
        }

        public Builder setData(List<AlbumInfo> data) {
            mAdapter.setList(data);
            // 滚动到选中的位置
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).isSelect()) {
                    mRecyclerView.scrollToPosition(i);
                }
            }
            return this;
        }

        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }


        @Override
        public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
            List<AlbumInfo> data = mAdapter.getData();
//            if (data.isEmpty()) {
//                return;
//            }

            for (AlbumInfo info : data) {
                if (info.isSelect()) {
                    info.setSelect(false);
                    break;
                }
            }
            mAdapter.getItem(position).setSelect(true);
            mAdapter.notifyDataSetChanged();

            // 延迟消失
            postDelayed(() -> {

                if (mListener != null) {
                    mListener.onSelected(getDialog(), position, mAdapter.getItem(position));
                }
                dismiss();

            }, 300);
        }
    }

    private static final class AlbumAdapter extends BaseQuickAdapter<AlbumInfo, BaseViewHolder> {

        private AlbumAdapter() {
            super(R.layout.item_album);
        }

        @Override
        protected void convert(BaseViewHolder helper, AlbumInfo item) {

            Glide.with(getContext())
                    .load(item.getIcon())
                    .into((ImageView) helper.itemView.findViewById(R.id.iv_album_icon));

            ((TextView) helper.itemView.findViewById(R.id.tv_album_name)).setText(item.getName());
            ((TextView) helper.itemView.findViewById(R.id.tv_album_remark)).setText(item.getRemark());

            ((CheckBox) helper.itemView.findViewById(R.id.rb_album_check)).setChecked(item.isSelect());
            helper.itemView.findViewById(R.id.rb_album_check).setVisibility(item.isSelect() ? View.VISIBLE : View.INVISIBLE);

        }
    }


    /**
     * 专辑信息类
     */
    public static class AlbumInfo {

        /**
         * 封面
         */
        private String icon;
        /**
         * 名称
         */
        private String name;
        /**
         * 备注
         */
        private String remark;
        /**
         * 选中
         */
        private boolean select;

        public AlbumInfo(String icon, String name, String remark, boolean select) {
            this.icon = icon;
            this.name = name;
            this.remark = remark;
            this.select = select;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public String getIcon() {
            return icon;
        }

        public String getName() {
            return name;
        }

        public String getRemark() {
            return remark;
        }

        public boolean isSelect() {
            return select;
        }
    }

    public interface OnListener {

        /**
         * 选择条目时回调
         */
        void onSelected(BaseDialog dialog, int position, AlbumInfo bean);
    }
}