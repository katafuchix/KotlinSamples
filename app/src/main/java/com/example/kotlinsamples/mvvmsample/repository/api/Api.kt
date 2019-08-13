package com.example.kotlinsamples.mvvmsample.repository.api

import com.example.kotlinsamples.mvvmsample.repository.data.User
import io.reactivex.Observable
import retrofit2.http.GET

interface UserApi {

    @GET("6de6abfedb24f889e0b5f675edc50deb?fmt=raw&sole")
    fun getUsers(): Observable<List<User>>
}
