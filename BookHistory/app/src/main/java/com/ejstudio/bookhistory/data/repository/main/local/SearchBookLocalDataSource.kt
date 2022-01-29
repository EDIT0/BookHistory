package com.ejstudio.bookhistory.data.repository.main.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.data.model.RecentSearchesEntity
import io.reactivex.Completable
import io.reactivex.rxjava3.core.Observable

interface SearchBookLocalDataSource {
    fun insertRecentSearches(recentSearches: RecentSearchesEntity) :io.reactivex.rxjava3.core.Completable
    fun getRecentSearchesList(email: String) : LiveData<List<RecentSearchesEntity>>
    fun deleteRecentSearches(deleteIdx: Int) : io.reactivex.rxjava3.core.Completable
    fun totalDeleteRecentSearches(email: String) : io.reactivex.rxjava3.core.Completable
}