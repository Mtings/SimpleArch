package com.song.sakura.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.song.sakura.R;
import com.song.sakura.aop.SingleClick;
import com.song.sakura.ui.base.BaseViewHolder;
import com.ui.action.AnimAction;
import com.ui.base.BaseDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/12/2
 * desc   : 菜单选择框
 */
public final class MenuDialog {

    public static final class Builder
            extends BaseDialog.Builder<Builder>
            implements OnItemClickListener {

        private OnListener mListener;
        private boolean mAutoDismiss = true;

        private final MenuAdapter mAdapter;
        private final TextView mCancelView;

        public Builder(Context context) {
            super(context);
            setContentView(R.layout.dialog_menu);
            setAnimStyle(AnimAction.BOTTOM);

            RecyclerView recyclerView = findViewById(R.id.rv_menu_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            mCancelView = findViewById(R.id.tv_menu_cancel);

            mAdapter = new MenuAdapter();
            mAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(mAdapter);

            setOnClickListener(R.id.tv_menu_cancel);
        }

        @Override
        public Builder setGravity(int gravity) {
            switch (gravity) {
                // 如果这个是在中间显示的
                case Gravity.CENTER:
                case Gravity.CENTER_VERTICAL:
                    // 不显示取消按钮
                    setCancel(null);
                    // 重新设置动画
                    setAnimStyle(AnimAction.SCALE);
                    break;
                default:
                    break;
            }
            return super.setGravity(gravity);
        }

        public Builder setList(int... ids) {
            List<String> data = new ArrayList<>(ids.length);
            for (int id : ids) {
                data.add(getString(id));
            }
            return setList(data);
        }

        public Builder setList(String... data) {
            return setList(Arrays.asList(data));
        }

        @SuppressWarnings("all")
        public Builder setList(List data) {
            mAdapter.setNewData(data);
            return this;
        }

        public Builder setCancel(@StringRes int id) {
            return setCancel(getString(id));
        }

        public Builder setCancel(CharSequence text) {
            mCancelView.setText(text);
            return this;
        }

        public Builder setAutoDismiss(boolean dismiss) {
            mAutoDismiss = dismiss;
            return this;
        }

        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }

        @SingleClick
        @Override
        public void onClick(View v) {
            if (mAutoDismiss) {
                dismiss();
            }

            if (v == mCancelView) {
                if (mListener != null) {
                    mListener.onCancel(getDialog());
                }
            }
        }

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            if (mAutoDismiss) {
                dismiss();
            }

            if (mListener != null) {
                mListener.onSelected(getDialog(), position, mAdapter.getItem(position));
            }
        }
    }

    private static final class MenuAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        private MenuAdapter() {
            super(R.layout.item_menu);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            ((TextView) helper.itemView.findViewById(R.id.tv_menu_text)).setText(item);
            View mLineView;
            mLineView = helper.itemView.findViewById(R.id.v_menu_line);

            if (helper.getLayoutPosition() == 0) {
                // 当前是否只有一个条目
                if (getItemCount() == 1) {
                    mLineView.setVisibility(View.GONE);
                } else {
                    mLineView.setVisibility(View.VISIBLE);
                }
            } else if (helper.getLayoutPosition() == getItemCount() - 1) {
                mLineView.setVisibility(View.GONE);
            } else {
                mLineView.setVisibility(View.VISIBLE);
            }
        }
    }

    public interface OnListener<T> {

        /**
         * 选择条目时回调
         */
        void onSelected(BaseDialog dialog, int position, T t);

        /**
         * 点击取消时回调
         */
        default void onCancel(BaseDialog dialog) {
        }
    }
}