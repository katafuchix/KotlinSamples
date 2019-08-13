package com.example.kotlinsamples.scores.data

import androidx.room.*
import io.reactivex.Flowable


/**
 * Data Access Object for the scores table.
 */
@Dao
interface ScoreDao {

    @Query("select * from scores")
    fun getAllScores(): Flowable<List<Score>>

    @Query("select * from scores where id in (:id)")
    fun getScoreById(id: Int):Score

    @Query("delete from scores")
    fun deleteAllScore()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertScore(score: Score)

    @Update
    fun updateScore(score: Score)

    @Delete
    fun deleteScore(score: Score)
}