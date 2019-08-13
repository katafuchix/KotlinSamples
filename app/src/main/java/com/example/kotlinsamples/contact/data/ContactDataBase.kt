package com.example.kotlinsamples.contact.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase



@Database(entities = [(Contact::class)], version = 1, exportSchema = false)
abstract class ContactDataBase : RoomDatabase() {
    companion object {
        private var INSTANCE: ContactDataBase? = null
        fun getDataBase(context: Context): ContactDataBase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, ContactDataBase::class.java, "contacts-db")
                    .allowMainThreadQueries().build()
            }
            return INSTANCE as ContactDataBase
        }
    }

    abstract fun contactDao(): ContactDao
}

/*
@Database(entities = [Contact::class], version = ContactDataBase.DATABASE_VERSION)
abstract class ContactDataBase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
    companion object {
        const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "contacts-db"
        private var instance: ContactDataBase? = null
        fun init(context: Context) {
            Room.databaseBuilder(context, ContactDataBase::class.java, DATABASE_NAME)
                .build().also { instance = it }
        }
        fun getInstance() = instance
    }
}
*/
