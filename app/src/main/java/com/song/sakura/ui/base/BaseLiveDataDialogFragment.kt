package com.song.sakura.ui.base

import android.content.Context
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.ui.base.BaseActivity
import com.ui.base.BaseViewModel

abstract class BaseLiveDataDialogFragment<T : BaseViewModel> : DialogFragment() {
    lateinit var baseActivity: BaseActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseActivity = context as BaseActivity
    }

    protected lateinit var mViewModel: T

    protected abstract fun observeErrorLiveData(viewModel: BaseViewModel) ;

    fun initViewModel(modelClass: Class<T>) {
        this.mViewModel = this.registerViewModel(modelClass, false, true)
    }

    fun initViewModel(modelClass: Class<T>, isSingle: Boolean) {
        this.mViewModel = this.registerViewModel(modelClass, isSingle, true)
    }

    fun initViewModel(modelClass: Class<T>, viewModelName: String) {
        this.mViewModel = this.registerViewModel<T>(modelClass, viewModelName, true)
    }

    fun initViewModel(modelClass: Class<T>, viewModelName: String, isRegisterError: Boolean) {
        this.mViewModel = this.registerViewModel(modelClass, viewModelName, isRegisterError)
    }

    fun initViewModel(modelClass: Class<T>, isSingle: Boolean, isRegisterError: Boolean) {
        this.mViewModel = this.registerViewModel(modelClass, isSingle, isRegisterError)
    }

    fun <Q : BaseViewModel> registerViewModel(modelClass: Class<Q>, isSingle: Boolean): Q {
        return this.registerViewModel(modelClass, isSingle, false)
    }

    fun <Q : BaseViewModel> registerViewModel(modelClass: Class<Q>, isSingle: Boolean, isRegisterError: Boolean): Q {
        var key: String? = modelClass.canonicalName
        if (key == null) {
            key = this.javaClass.canonicalName
        }

        return this.registerViewModel(modelClass, if (isSingle) "$this:$key" else key!!, isRegisterError)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <Q : BaseViewModel> registerViewModel(modelClass: Class<Q>, viewModelName: String, isRegisterError: Boolean): Q {
        val mViewModel = ViewModelProviders.of(baseActivity).get(viewModelName, modelClass) as BaseViewModel
        if (isRegisterError) {
            this.observeErrorLiveData(mViewModel)
        }

        return mViewModel as Q
    }

    fun <Q : BaseViewModel> registerViewModel(modelClass: Class<Q>): Q {
        return this.registerViewModel(modelClass, true)
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }
}