package com.song.sakura.ui.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.gyf.immersionbar.ImmersionBar;

import com.network.api.ApiResponse;
import com.song.sakura.app.App;
import com.song.sakura.ui.main.ShareViewModel;
import com.ui.base.BaseFragment;
import com.ui.base.BaseViewModel;

public class IBaseFragment<T extends BaseViewModel> extends BaseFragment {

    public static <B> Boolean isSuccess(ApiResponse<B> it) {
        return null != it && it.getErrorCode() == 0;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void initImmersionBar() {
        try {
            ImmersionBar.with(this).keyboardEnable(true).statusBarDarkFont(true)
                    .navigationBarWithKitkatEnable(false).init();
        } catch (Exception ignored) {
        }
    }

    protected T mViewModel;
    protected ShareViewModel mShareViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShareViewModel = getAppViewModelProvider().get(ShareViewModel.class);
    }

    public void initViewModel(Fragment fragment, Class<T> modelClass) {
        this.mViewModel = getFragmentViewModelProvider(fragment).get(modelClass);
    }

    public void initActivityViewModel(AppCompatActivity activity, Class<T> modelClass) {
        this.mViewModel = getActivityViewModelProvider(activity).get(modelClass);
    }

    protected ViewModelProvider getAppViewModelProvider() {
        return ((App) getBaseActivity().getApplicationContext()).getAppViewModelProvider(getBaseActivity());
    }

    protected ViewModelProvider getFragmentViewModelProvider(Fragment fragment) {
        return new ViewModelProvider(fragment, fragment.getDefaultViewModelProviderFactory());
    }

    protected ViewModelProvider getActivityViewModelProvider(AppCompatActivity activity) {
        return new ViewModelProvider(activity, activity.getDefaultViewModelProviderFactory());
    }

    protected NavController nav() {
        return NavHostFragment.findNavController(this);
    }

    public ShareViewModel getSharedViewModel() {
        return mShareViewModel;
    }

}
