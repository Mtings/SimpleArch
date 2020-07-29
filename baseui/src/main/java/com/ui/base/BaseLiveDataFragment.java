package com.ui.base;

import androidx.lifecycle.ViewModelProviders;

public class BaseLiveDataFragment<T extends BaseViewModel> extends BaseFragment {
    protected T mViewModel;

    public void initViewModel(Class<T> modelClass) {
        this.mViewModel = registerViewModel(modelClass, false,true);//ViewModelProviders.of(getBaseActivity()).get(this.toString() + ":" + modelClass, modelClass);
    }

    public <Q extends BaseViewModel> Q registerFragmentViewModel(Class<Q> modelClass, String viewModelName) {
        Q mViewModel = ViewModelProviders.of(this).get(viewModelName, modelClass);
        return mViewModel;
    }
    public <Q extends BaseViewModel> Q registerViewModel(Class<Q> modelClass, boolean isSingle) {
        return registerViewModel(modelClass, isSingle, false);
    }

    public <Q extends BaseViewModel> Q registerViewModel(Class<Q> modelClass, boolean isSingle, boolean isRegisterError) {
        String key = modelClass.getCanonicalName();
        if (key == null) {
            key = getClass().getCanonicalName();
        }
        return registerViewModel(modelClass,isSingle ? (this.toString() + ":" + key) : key,isRegisterError);
    }
    public <Q extends BaseViewModel> Q registerViewModel(Class<Q> modelClass, String viewModelName, boolean isRegisterError) {
        return ViewModelProviders.of(getBaseActivity()).get(viewModelName, modelClass);
    }

    public <Q extends BaseViewModel> Q registerViewModel(Class<Q> modelClass) {
        return registerViewModel(modelClass, true);
    }

}
