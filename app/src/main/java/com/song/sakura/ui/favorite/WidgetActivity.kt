package com.song.sakura.ui.favorite

import android.os.Bundle
import com.song.sakura.R
import com.song.sakura.ui.base.IBaseActivity
import com.song.sakura.ui.base.IBaseViewModel
import kotlinx.android.synthetic.main.activity_widget.*

class WidgetActivity : IBaseActivity<IBaseViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget)
        initImmersionBar()
        mToolbar?.apply {
            title = "Widget"
        }

        countdownText.startCountDown("汉")
        edit.setText("汉字")

        button.setOnClickListener {
            countdownText.resetCountdownTimer()
            countdownText.startCountDown(edit.text.toString())
        }
    }


}