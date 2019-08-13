package com.example.kotlinsamples.contact.viewmodel

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinsamples.contact.data.Contact
import com.example.kotlinsamples.contact.data.ContactDao
import com.example.kotlinsamples.contact.data.ContactDataBase
import com.example.kotlinsamples.scores.data.ScoreDataBase
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

/**
 * View Model for the [ContactFragment]
 */
class ContactViewModel(
    private val dataSource: ContactDao
) : ViewModel() {

    var listContact: LiveData<List<Contact>>

    init {
        listContact = dataSource.getAllContacts()
    }


    fun getListContacts(): LiveData<List<Contact>> {
        return listContact
    }

    fun addContact(contact: Contact) {
        Completable.fromAction { dataSource.insertContact(contact) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun updateContact(contact: Contact) {
        Completable.fromAction { dataSource.updateContact(contact) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun deleteContact(contact: Contact) {
        Completable.fromAction { dataSource.deleteContact(contact) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    class addAsynTask(db: ContactDataBase) : AsyncTask<Contact, Void, Void>() {
        private var contactDb = db
        override fun doInBackground(vararg params: Contact): Void? {
            contactDb.contactDao().insertContact(params[0])
            return null
        }
    }

    class Factory(
        private val dataSource: ContactDao
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ContactViewModel( dataSource ) as T
        }
    }
}