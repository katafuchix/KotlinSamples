package com.example.kotlinsamples.mvvmsample.repository.data

import androidx.room.*

@Database(entities = arrayOf(User::class), version = 1)
abstract class UserDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao
}