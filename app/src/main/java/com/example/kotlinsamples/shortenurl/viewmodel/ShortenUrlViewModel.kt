package com.example.kotlinsamples.shortenurl.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinsamples.shortenurl.model.Repository
import com.rengwuxian.materialedittext.validation.METValidator
import com.rengwuxian.materialedittext.validation.RegexpValidator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ShortenUrlViewModel(
    private val repository: Repository
): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _shortenUrl = MutableLiveData<String>()
    private val _error = MutableLiveData<String>()

    val showResult = MutableLiveData<Boolean>()

    // LiveData
    val shortenUrl: LiveData<String> get() = _shortenUrl
    val error: LiveData<String> get() = _error

    fun getUrlValidator(errorMessage: String): METValidator {
        return RegexpValidator(errorMessage, Patterns.WEB_URL.pattern())
    }

    fun getShortenUrl(url: String) {
        addDisposable(repository.getShortenUrl(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                showResult.value = true
                _shortenUrl.value = it.url
            }, {
                _error.value = it.message
            }))
    }

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    class Factory(
        private val repository: Repository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ShortenUrlViewModel( repository ) as T
        }
    }
}