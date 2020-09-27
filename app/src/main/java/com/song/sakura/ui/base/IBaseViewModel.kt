package com.song.sakura.ui.base

import android.app.Application
import com.song.sakura.network.ServiceModule
import com.ui.base.BaseViewModel

open class IBaseViewModel(application: Application) :
    BaseViewModel(application) {
    protected val api = ServiceModule.Singleton.serviceModule.getApiService()

}