package com.song.sakura.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.song.sakura.R
import com.song.sakura.aop.SingleClick
import com.song.sakura.ui.base.IBaseFragment
import com.song.sakura.ui.base.IBaseViewModel
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : IBaseFragment<IBaseViewModel>(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initImmersionBar()
        mToolbar?.apply {
            navigationIcon = null
        }

        btn_flash_view.setOnClickListener(this)
    }

    @SingleClick
    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_flash_view) {
            startActivity(Intent(getBaseActivity(), WidgetActivity::class.java))
        }
    }


}