package com.example.kotlinsamples.mvvmsample.repository.data

import androidx.room.*

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "firstName")
    val first: String,
    @ColumnInfo(name = "lastName")
    val last: String
)