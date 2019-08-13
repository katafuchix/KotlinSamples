package com.example.kotlinsamples.shortenurl.model

import io.reactivex.Single

interface Repository {
    fun getShortenUrl(url: String): Single<ShortenUrl>
}