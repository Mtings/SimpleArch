package com.song.sakura.ui.base

import com.ui.base.BaseViewModel

abstract class IBaseLazyFragment<Q : BaseViewModel> : IBaseFragment<Q>() {

    //ViewPager下懒加载

    private var isLoaded = false

    override fun onResume() {
        super.onResume()
        if (!isLoaded && !isHidden) {
            lazyLoad()
            isLoaded = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
    }

    abstract fun lazyLoad()
}
