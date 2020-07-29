package com.song.sakura.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;

import com.hjq.toast.ToastUtils;
import com.ui.base.BaseActivity;
import com.ui.base.BaseViewModel;
import com.ui.util.DialogUtil;

import com.song.sakura.BuildConfig;
import com.song.sakura.R;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

@SuppressWarnings({"deprecation", "unchecked"})
public abstract class IBaseDialogFragment<T extends BaseViewModel> extends BaseLiveDataDialogFragment<T> {
    public final CompositeDisposable subscription = new CompositeDisposable();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IBaseActivity) {
            baseActivity = (IBaseActivity) context;
        }
    }

    protected <T extends View> T getView(View view, @IdRes int resId) {
        T t = (T) view.findViewById(resId);
        if (t == null) {
            throw new IllegalArgumentException("view 0x" + Integer.toHexString(resId)
                    + " doesn't exist");
        }
        return t;
    }

    protected <Q extends View> Q findViewById(@IdRes int resId) {
        Q t = null;
        if (getView() != null)
            t = getView().findViewById(resId);
        if (t == null) {
            throw new IllegalArgumentException("view 0x" + Integer.toHexString(resId)
                    + " doesn't exist");
        }
        return t;
    }

    protected <Q extends View> Q getView(Activity view, @IdRes int resId) {
        Q t = view.findViewById(resId);
        if (t == null) {
            return null;
        }
        return t;
    }

    public Fragment findFragment(String tag) {
        return getChildFragmentManager().findFragmentByTag(tag);
    }


    public int getColors(@ColorRes int resId) {
        return getResources().getColor(resId);
    }

    protected int getColor(@ColorRes int resId) {
        return getResources().getColor(resId);
    }

    public void setProgressVisible(boolean isVisible) {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        if (baseActivity != null) {
            baseActivity.setProgressVisible(isVisible);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public void startActivity(Intent intent, boolean isBack) {
        super.startActivity(intent);
        if (isBack) {
            getActivity().overridePendingTransition(R.anim.left_in, R.anim.right_out);
        } else {
            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }

    protected void finish() {
        getActivity().finish();
    }

    public void startActivity(Class clz) {
        Intent intent = new Intent(getActivity(), FragmentParentActivity.class);
        intent.putExtra(FragmentParentActivity.KEY_FRAGMENT, clz);
        startActivity(intent);
    }

    public void startActivity(Class clz, boolean isToolbar) {
        Intent intent = new Intent(getActivity(), FragmentParentActivity.class);
        intent.putExtra(FragmentParentActivity.KEY_FRAGMENT, clz);
        intent.putExtra(FragmentParentActivity.KEY_TOOLBAR, isToolbar);
        startActivity(intent);
    }

    public void error(String error) {
        setProgressVisible(false);
        if (this.isDetached()) {
            return;
        }
        DialogUtil.createDialogView(getActivity(), error, (dialog, which) -> {
            dialog.dismiss();
        }, R.string.btn_confirm);
    }

    public void error(int code, String error) {
        error(error);
    }

    public void errorNoCancel(String error) {
        setProgressVisible(false);
        DialogUtil.createDialogView(getActivity(), error, (dialog, which) -> {
            dialog.dismiss();
            getActivity().finish();
        }, R.string.btn_confirm, false);
    }

    public void dismissKeyboard() {
        getBaseActivity().dismissKeyboard();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public <T> void bindUi(Observable<T> observable, Consumer<? super T> onNext, Consumer<Throwable> onError) {
        subscription.add(observable.observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError));
    }

    public <T> void bindUi(Observable<T> observable, Consumer<? super T> onNext) {
        subscription.add(observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, throwable -> {
                            if (BuildConfig.DEBUG)
                                ToastUtils.show(throwable.getMessage());
                        }
                ));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.clear();
    }
}
