package com.song.sakura.ui.base

import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.gyf.immersionbar.ImmersionBar
import com.network.api.ApiResponse
import com.song.sakura.R
import com.song.sakura.app.App
import com.song.sakura.ui.main.ShareViewModel
import com.ui.base.BaseFragment
import com.ui.base.BaseViewModel

open class IBaseFragment<T : BaseViewModel> : BaseFragment() {

    companion object {
        fun <B> isSuccess(it: ApiResponse<B>?): Boolean {
            return null != it && it.errorCode == 0
        }
    }

    open fun initImmersionBar() {
        try {
            ImmersionBar.with(this).keyboardEnable(true)
                .statusBarDarkFont(true)
                .titleBar(R.id.appbar)
                .init()
        } catch (ignored: Exception) {
        }
    }

    lateinit var mViewModel: T
    lateinit var sharedViewModel: ShareViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = getAppViewModelProvider().get(ShareViewModel::class.java)
    }

    fun initViewModel(fragment: Fragment, modelClass: Class<T>) {
        mViewModel = getFragmentViewModelProvider(fragment)[modelClass]
    }

    fun initActivityViewModel(activity: AppCompatActivity, modelClass: Class<T>) {
        mViewModel = getActivityViewModelProvider(activity)[modelClass]
    }

    protected fun getAppViewModelProvider(): ViewModelProvider {
        return (getBaseActivity().applicationContext as App).getAppViewModelProvider(getBaseActivity())
    }

    protected fun getFragmentViewModelProvider(fragment: Fragment): ViewModelProvider {
        return ViewModelProvider(fragment, fragment.defaultViewModelProviderFactory)
    }

    protected fun getActivityViewModelProvider(activity: AppCompatActivity): ViewModelProvider {
        return ViewModelProvider(activity, activity.defaultViewModelProviderFactory)
    }

    protected fun nav(): NavController {
        return NavHostFragment.findNavController(this)
    }

}