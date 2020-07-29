package com.network.api

import android.annotation.SuppressLint
import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

class RetrofitModule {
    private var context: Context? = null
    lateinit var client: OkHttpClient
    lateinit var retrofit: Retrofit

    @SuppressLint("StaticFieldLeak")
    object Singleton {
        val retrofitModule: RetrofitModule = RetrofitModule()
    }

    private val TIMEOUT = 30L
    private val clientBuilder: OkHttpClient.Builder =
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)

    private val retrofitBuilder = Retrofit.Builder()
//        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())


    fun addInterceptor(interceptor: Interceptor): RetrofitModule {
        clientBuilder.addInterceptor(interceptor)
        return this
    }


    fun setTimeOut(timeout: Long): RetrofitModule {
        clientBuilder.connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
        return this
    }

    fun addCallAdapterFactory(factory: CallAdapter.Factory): RetrofitModule {
        retrofitBuilder.addCallAdapterFactory(factory)
        return this
    }

    fun addConverterFactory(factory: Converter.Factory): RetrofitModule {
        retrofitBuilder.addConverterFactory(factory)
        return this
    }

    fun setBaseUrl(baseUrl: String): RetrofitModule {
        retrofitBuilder.baseUrl(baseUrl)
        return this
    }

    fun setDebug(debug: Boolean): RetrofitModule {
        if (debug) {
            clientBuilder.addInterceptor(LoggingInterceptor())
        }
        return this
    }

    fun getContext() = context;

    fun setContext(context: Context): RetrofitModule {
        this.context = context
        return this
    }

    fun build(): RetrofitModule {
        client = clientBuilder.build()
        retrofit = retrofitBuilder.client(client)
            .build()
        return this
    }

}