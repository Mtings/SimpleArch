package com.song.sakura.ui.home

import android.os.Bundle
import com.song.sakura.R
import com.song.sakura.ui.base.IBaseActivity
import com.song.sakura.ui.base.IBaseViewModel
import com.ui.action.ClickAction

class SearchActivity : IBaseActivity<IBaseViewModel>(), ClickAction {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initImmersionBar()
    }


}