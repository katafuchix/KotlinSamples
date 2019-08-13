package com.example.kotlinsamples.mvvmsample.viewmodel.data

import com.example.kotlinsamples.mvvmsample.repository.data.User

data class UsersList(val users: List<User>, val message: String, val error: Throwable? = null)
