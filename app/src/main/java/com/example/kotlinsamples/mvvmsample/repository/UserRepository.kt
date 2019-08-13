package com.example.kotlinsamples.mvvmsample.repository

import com.example.kotlinsamples.mvvmsample.repository.api.UserApi
import com.example.kotlinsamples.mvvmsample.repository.data.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class UserRepository(val userApi: UserApi, val userDao: UserDao) {

    fun getUsers(): Observable<List<User>> {
        return Observable.concatArray(
            getUsersFromDb(),
            getUsersFromApi())
    }


    fun getUsersFromDb(): Observable<List<User>> {
        return userDao.getUsers().filter { it.isNotEmpty() }
            .toObservable()
            .doOnNext {
                Timber.d("Dispatching ${it.size} users from DB...")
            }
    }

    fun getUsersFromApi(): Observable<List<User>> {
        return userApi.getUsers()
            .doOnNext {
                Timber.d("Dispatching ${it.size} users from API...")
                storeUsersInDb(it)
            }
    }

    fun storeUsersInDb(users: List<User>) {
        Observable.fromCallable { userDao.insertAll(users) }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                Timber.d("Inserted ${users.size} users from API in DB...")
            }
    }

}
