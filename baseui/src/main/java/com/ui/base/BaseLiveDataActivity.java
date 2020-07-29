package com.ui.base;

import androidx.lifecycle.ViewModelProviders;

public class BaseLiveDataActivity<T extends BaseViewModel> extends BaseActivity {
    protected T mViewModel;

    public void initViewModel(Class<T> modelClass) {
        this.mViewModel = registerViewModel(modelClass, false);
    }

    public void initViewModel(Class<T> modelClass, boolean isSingle) {
        this.mViewModel = registerViewModel(modelClass, isSingle);
    }

    public void initViewModel(Class<T> modelClass, String viewModelName) {
        this.mViewModel = registerViewModel(modelClass, viewModelName);
    }

    public <Q extends BaseViewModel> Q registerViewModel(Class<Q> modelClass, boolean isSingle) {
        String key = modelClass.getCanonicalName();
        if (key == null) {
            key = getClass().getCanonicalName();
        }
        return registerViewModel(modelClass, isSingle ? (this.toString() + ":" + key) : key);
    }

    public <Q extends BaseViewModel> Q registerViewModel(Class<Q> modelClass, String viewModelName) {
        Q mViewModel = ViewModelProviders.of(this).get(viewModelName, modelClass);
        return mViewModel;
    }

    public <Q extends BaseViewModel> Q registerViewModel(Class<Q> modelClass) {
        return registerViewModel(modelClass, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
