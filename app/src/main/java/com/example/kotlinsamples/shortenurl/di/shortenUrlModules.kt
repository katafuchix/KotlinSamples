package com.example.kotlinsamples.shortenurl.di

import com.example.kotlinsamples.shortenurl.model.NetworkRepositoryImpl
import com.example.kotlinsamples.shortenurl.model.Repository
import com.example.kotlinsamples.shortenurl.viewmodel.ShortenUrlViewModelFactory
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

val shortenUrlModules: Module = module {
    factory {
        NetworkRepositoryImpl(get()) as Repository
    }

    factory {
        ShortenUrlViewModelFactory(get())
    }
}
