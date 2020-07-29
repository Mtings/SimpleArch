package com.song.sakura.ui.base

import android.os.Bundle
import android.view.View
import com.ui.base.BaseViewModel

abstract class IBaseLazyFragment<Q : BaseViewModel> : IBaseFragment<Q>() {


    @JvmField
    protected var isVisibled: Boolean = false
    protected var isPrepared: Boolean = false   // 标志位，标志已经初始化完成。
    /**
     * 是否已被加载过一次，第二次就不再去请求数据
     */
    private var mHasLoaded: Boolean = false

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            isVisibled = true
            onVisible()
        } else {
            isVisibled = false
            onInVisible()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isPrepared = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    protected fun isLazyLoad(): Boolean {
        return !(!isPrepared || !isVisibled || mHasLoaded)
    }

    protected fun onVisible() {
        if (isLazyLoad()) lazyLoad()
    }

    protected fun onInVisible() {

    }

    abstract fun lazyLoad()

    fun isHasLoaded(): Boolean {
        return mHasLoaded
    }

    fun setHasLoaded(hasLoaded: Boolean) {
        this.mHasLoaded = hasLoaded
    }
}
