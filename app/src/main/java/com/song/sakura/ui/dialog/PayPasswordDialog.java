package com.song.sakura.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.song.sakura.R;
import com.song.sakura.aop.SingleClick;
import com.song.sakura.ui.base.BaseViewHolder;
import com.ui.base.BaseDialog;
import com.ui.widget.view.PasswordView;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/12/2
 * desc   : 支付密码对话框
 */
public final class PayPasswordDialog {

    public static final class Builder
            extends BaseDialog.Builder<Builder>
            implements OnItemClickListener {

        /**
         * 输入键盘文本
         */
        private static final String[] KEYBOARD_TEXT = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", ""};

        private OnListener mListener;
        private boolean mAutoDismiss = true;
        private final LinkedList<String> mRecordList = new LinkedList<>();

        private final TextView mTitleView;
        private final ImageView mCloseView;

        private final TextView mSubTitleView;
        private final TextView mMoneyView;

        private final PasswordView mPasswordView;

        private final KeyboardAdapter mAdapter;

        public Builder(Context context) {
            super(context);
            setContentView(R.layout.dialog_pay_password);
            setCancelable(false);

            mTitleView = findViewById(R.id.tv_pay_title);
            mCloseView = findViewById(R.id.iv_pay_close);

            mSubTitleView = findViewById(R.id.tv_pay_sub_title);
            mMoneyView = findViewById(R.id.tv_pay_money);

            mPasswordView = findViewById(R.id.pw_pay_view);

            RecyclerView recyclerView = findViewById(R.id.rv_pay_list);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
            mAdapter = new KeyboardAdapter();

            mAdapter.setNewData(Arrays.asList(KEYBOARD_TEXT));
            mAdapter.setOnItemClickListener(this);

            recyclerView.setAdapter(mAdapter);

            setOnClickListener(R.id.iv_pay_close);
        }

        public Builder setTitle(@StringRes int id) {
            return setTitle(getString(id));
        }

        public Builder setTitle(CharSequence title) {
            mTitleView.setText(title);
            return this;
        }

        public Builder setSubTitle(@StringRes int id) {
            return setSubTitle(getString(id));
        }

        public Builder setSubTitle(CharSequence subTitle) {
            mSubTitleView.setText(subTitle);
            return this;
        }

        public Builder setMoney(@StringRes int id) {
            return setSubTitle(getString(id));
        }

        public Builder setMoney(CharSequence money) {
            mMoneyView.setText(money);
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


        /**
         * {@link OnItemClickListener}
         */
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            switch (position) {
                case KeyboardAdapter.TYPE_DELETE:
                    // 点击回退按钮删除
                    if (mRecordList.size() != 0) {
                        mRecordList.removeLast();
                    }
                    break;
                case KeyboardAdapter.TYPE_EMPTY:
                    // 点击空白的地方不做任何操作
                    break;
                default:
                    // 判断密码是否已经输入完毕
                    if (mRecordList.size() < PasswordView.PASSWORD_COUNT) {
                        // 点击数字，显示在密码行
                        mRecordList.add(KEYBOARD_TEXT[position]);
                    }

                    // 判断密码是否已经输入完毕
                    if (mRecordList.size() == PasswordView.PASSWORD_COUNT) {
                        if (mListener != null) {
                            postDelayed(() -> {

                                if (mAutoDismiss) {
                                    dismiss();
                                }
                                // 获取输入的支付密码
                                StringBuilder password = new StringBuilder();
                                for (String s : mRecordList) {
                                    password.append(s);
                                }
                                mListener.onCompleted(getDialog(), password.toString());

                            }, 300);
                        }
                    }
                    break;
            }
            mPasswordView.setPassWordLength(mRecordList.size());
        }

        @SingleClick
        @Override
        public void onClick(View v) {
            if (v == mCloseView) {
                if (mAutoDismiss) {
                    dismiss();
                }

                if (mListener != null) {
                    mListener.onCancel(getDialog());
                }
            }
        }
    }

    private static final class KeyboardAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        /**
         * 删除按钮条目位置
         */
        private static final int TYPE_DELETE = 11;
        /**
         * 空按钮条目位置
         */
        private static final int TYPE_EMPTY = 9;

        private KeyboardAdapter() {
            super(R.layout.item_pay_password);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            switch (helper.getLayoutPosition()) {
                case TYPE_EMPTY: //空按钮
                    helper.findViewById(R.id.tv_pay_key).setVisibility(View.GONE);
                    helper.findViewById(R.id.iv_delete).setVisibility(View.GONE);
                    helper.itemView.setBackground(null);
                    break;
                case TYPE_DELETE: //删除按钮
                    helper.findViewById(R.id.tv_pay_key).setVisibility(View.GONE);
                    helper.findViewById(R.id.iv_delete).setVisibility(View.VISIBLE);
                    helper.itemView.setBackgroundResource(R.drawable.selector_transparent);
                    break;
                default: //数字按钮
                    helper.findViewById(R.id.tv_pay_key).setVisibility(View.VISIBLE);
                    helper.findViewById(R.id.iv_delete).setVisibility(View.GONE);
                    ((TextView) helper.findViewById(R.id.tv_pay_key)).setText(item);
                    helper.itemView.setBackgroundResource(R.drawable.selector_transparent);
                    break;
            }
        }
    }

    public interface OnListener {

        /**
         * 输入完成时回调
         *
         * @param password 输入的密码
         */
        void onCompleted(BaseDialog dialog, String password);

        /**
         * 点击取消时回调
         */
        default void onCancel(BaseDialog dialog) {
        }
    }
}