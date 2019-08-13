package com.example.kotlinsamples.scores.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * The Room database that contains the scores table
 */
@Database(entities = arrayOf(Score::class), version = 1)
abstract class ScoreDataBase : RoomDatabase() {

    abstract fun scoreDao(): ScoreDao

    companion object {

        @Volatile private var INSTANCE: ScoreDataBase? = null

        fun getInstance(context: Context): ScoreDataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                ScoreDataBase::class.java, "Sample.db")
                .build()
    }
}

/*
@Database(entities = [Score::class], version = SampleDatabase.DATABASE_VERSION)
abstract class SampleDatabase : RoomDatabase() {
    abstract fun scoreDao(): ScoreDao
    companion object {
        const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "sample.db"
        private var instance: SampleDatabase? = null
        fun init(context: Context) {
            Room.databaseBuilder(context, SampleDatabase::class.java, DATABASE_NAME)
                .build().also { instance = it }
        }
        fun getInstance() = instance
    }
}
*/