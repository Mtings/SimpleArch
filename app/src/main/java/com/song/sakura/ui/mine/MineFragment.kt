package com.song.sakura.ui.mine

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.song.sakura.R
import com.song.sakura.aop.SingleClick
import com.song.sakura.ui.base.IBaseFragment
import com.song.sakura.ui.base.IBaseViewModel
import com.song.sakura.ui.message.DialogActivity
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : IBaseFragment<IBaseViewModel>(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initImmersionBar()
        mToolbar?.apply {
            title = "我的"
            navigationIcon = null
        }

        btnAop.setOnClickListener(this)
        btnDialog.setOnClickListener(this)
    }

    @SingleClick
    override fun onClick(v: View?) {
        if (v?.id == R.id.btnAop) {
            val intent = Intent(baseActivity, AopActivity::class.java)
            startActivity(intent)
        } else if (v?.id == R.id.btnDialog) {
            val intent = Intent(getBaseActivity(), DialogActivity::class.java)
            startActivity(intent)
        }
    }

}