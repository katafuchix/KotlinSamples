package com.example.kotlinsamples.shortenurl.di

import com.example.kotlinsamples.BuildConfig
import com.example.kotlinsamples.R
import com.example.kotlinsamples.shortenurl.api.Api
import com.example.kotlinsamples.shortenurl.utils.baseUrl
import com.example.kotlinsamples.shortenurl.utils.headerInterceptor
import com.example.kotlinsamples.shortenurl.utils.loggingInterceptor
import io.reactivex.schedulers.Schedulers.single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val apiModules: Module = module {
    single {
        Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(get(headerInterceptor))
                    .addInterceptor(get(loggingInterceptor))
                    .build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
            .create(Api::class.java)
    }

    single(headerInterceptor) {
        Interceptor { chain ->
            val builder = chain.request().newBuilder().apply {
                header("X-Naver-Client-Id", androidContext().resources.getString(R.string.naver_client_id))
                header("X-Naver-Client-Secret", androidContext().resources.getString(R.string.naver_api_key))
            }
            chain.proceed(builder.build())
        }
    }

    single(loggingInterceptor) {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BASIC
            else
                HttpLoggingInterceptor.Level.NONE
        } as Interceptor
    }
}

val appModules = listOf(apiModules, shortenUrlModules)
