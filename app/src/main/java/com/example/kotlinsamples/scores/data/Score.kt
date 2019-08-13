package com.example.kotlinsamples.scores.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by K.Katafuchi on 10/08/2019.
 */
@Entity(tableName = "scores")
data class Score (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Int,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "score")
    var score : Int
)

/*
@Entity(tableName = "scores")
class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Int = 0

    @ColumnInfo(name = "name")
    var name: String = ""

    @ColumnInfo(name = "score")
    var score : Int = 0
}
*/