package com.song.sakura.ui.center

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.song.sakura.event.OpenDrawerEvent
import com.song.sakura.ui.base.IBaseViewModel
import org.greenrobot.eventbus.EventBus

/**
 * Title: com.song.sakura.ui.home
 * Description:
 * Copyright:Copyright(c) 2020
 * CreateTime:2020/03/26 15:56
 *
 * @author SogZiw
 * @version 1.0
 */

class CenterViewModel(app: Application) : IBaseViewModel(app) {

    val title = MutableLiveData<String>()

    fun initData() {
        title.value = "ACGPlayer"
    }



}