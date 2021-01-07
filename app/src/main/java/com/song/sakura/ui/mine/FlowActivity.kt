package com.song.sakura.ui.mine

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.helper.widget.Flow.*
import com.song.sakura.R
import com.song.sakura.aop.SingleClick
import com.song.sakura.ui.base.IBaseActivity
import com.song.sakura.ui.base.IBaseViewModel
import com.ui.action.ClickAction
import kotlinx.android.synthetic.main.activity_flow.*

class FlowActivity : IBaseActivity<IBaseViewModel>(), ClickAction {

    override fun isImmersionBarEnabled(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow)
        mToolbar?.title = "Flow"

        setOnClickListener(btn)
    }

    private var currentMode = WRAP_CHAIN

    override fun onClick(v: View?) {
        if (v == btn) {
            currentMode = when(currentMode) {
                WRAP_CHAIN -> {
                    flow.setWrapMode(WRAP_ALIGNED)
                    WRAP_ALIGNED
                }
                WRAP_ALIGNED -> {
                    flow.setWrapMode(WRAP_NONE)
                    WRAP_NONE
                }
                else -> {
                    flow.setWrapMode(WRAP_CHAIN)
                    WRAP_CHAIN
                }
            }
        }
    }

}