package com.song.sakura.ui.dialog;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
            implements OnItemClickListener, View.OnLayoutChangeListener, Runnable {

        private OnListener mListener;
        private boolean mAutoDismiss = true;

        private final RecyclerView mRecyclerView;
        private final TextView mCancelView;

        private final MenuAdapter mAdapter;


        public Builder(Context context) {
            super(context);
            setContentView(R.layout.dialog_menu);
            setAnimStyle(AnimAction.ANIM_BOTTOM);


            mRecyclerView = findViewById(R.id.rv_menu_list);
            mCancelView = findViewById(R.id.tv_menu_cancel);

            mAdapter = new MenuAdapter();
            mAdapter.setOnItemClickListener(this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            mRecyclerView.setAdapter(mAdapter);

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
                    setAnimStyle(AnimAction.ANIM_TOAST);
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
            mAdapter.setList(data);
            mRecyclerView.addOnLayoutChangeListener(this);
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

        @SuppressWarnings("all")
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            if (mAutoDismiss) {
                dismiss();
            }

            if (mListener != null) {
                mListener.onSelected(getDialog(), position, mAdapter.getItem(position));
            }
        }


        /**
         * {@link View.OnLayoutChangeListener}
         */
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            mRecyclerView.removeOnLayoutChangeListener(this);
            // 这里一定要加延迟，如果不加在 Android 9.0 上面会导致 setLayoutParams 无效
            post(this);
        }

        @Override
        public void run() {
            final ViewGroup.LayoutParams params = mRecyclerView.getLayoutParams();
            final int maxHeight = getScreenHeight() / 4 * 3;
            if (mRecyclerView.getHeight() > maxHeight) {
                if (params.height != maxHeight) {
                    params.height = maxHeight;
                    mRecyclerView.setLayoutParams(params);
                }
            } else {
                if (params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    mRecyclerView.setLayoutParams(params);
                }
            }
        }

        /**
         * 获取屏幕的高度
         */
        private int getScreenHeight() {
            WindowManager manager = getSystemService(WindowManager.class);
            DisplayMetrics outMetrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(outMetrics);
            return outMetrics.heightPixels;
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