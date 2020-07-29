package com.song.sakura.network

import com.network.api.LiveDataCallAdapterFactory
import com.network.api.NullOnEmptyConverterFactory
import com.network.api.RetrofitModule
import com.song.sakura.BuildConfig
import com.song.sakura.app.App
import retrofit2.converter.gson.GsonConverterFactory

class ServiceModule {
    object Singleton {
        val serviceModule: ServiceModule = ServiceModule()
    }

    init {
        RetrofitModule.Singleton.retrofitModule
            .addInterceptor(CommonInterceptor())
            .setDebug(BuildConfig.DEBUG)
            .setContext(App.getApplication())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
//            .addConverterFactory(NullOnEmptyConverterFactory())
            .setBaseUrl(BuildConfig.BASE_URL)
            .build()
    }

    fun getApiService(): ApiService =
        RetrofitModule.Singleton.retrofitModule.retrofit.create(ApiService::class.java)

}