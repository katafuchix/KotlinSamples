package com.example.kotlinsamples.shortenurl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinsamples.shortenurl.model.Repository

@Suppress("UNCHECKED_CAST")
class ShortenUrlViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ShortenUrlViewModel(repository) as T
    }
}