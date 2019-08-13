package com.example.kotlinsamples.mvvmsample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinsamples.mvvmsample.repository.UserRepository
import com.example.kotlinsamples.mvvmsample.viewmodel.data.UsersList
import io.reactivex.Observable
import timber.log.Timber

class UserListViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    fun getUsers(): Observable<UsersList> {
        //Create the data for your UI, the users lists and maybe some additional data needed as well
        return userRepository.getUsers()
            .map {
                Timber.d("Mapping users to UIData...")
                UsersList(it.take(10), "Top 10 Users")
            }
            .onErrorReturn {
                Timber.d("An error occurred $it")
                UsersList(emptyList(), "An error occurred", it)
            }
    }

    class Factory(
        private val userRepository: UserRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UserListViewModel( userRepository ) as T
        }
    }
}
