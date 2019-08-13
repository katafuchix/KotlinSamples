package com.example.kotlinsamples.shortenurl.model

import com.example.kotlinsamples.shortenurl.api.Api
import io.reactivex.Single

class NetworkRepositoryImpl(private val api: Api): Repository {
    override fun getShortenUrl(url: String): Single<ShortenUrl> {
        return api.shorturl(url)
            .map { shortenUrlResponse ->
                shortenUrlResponse.result
            }
    }
}