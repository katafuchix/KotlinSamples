package com.example.kotlinsamples.shortenurl.api

import com.example.kotlinsamples.shortenurl.model.ShortenUrl

data class ShortenUrlResponse(val message: String,
                              val result: ShortenUrl,
                              val code: String)