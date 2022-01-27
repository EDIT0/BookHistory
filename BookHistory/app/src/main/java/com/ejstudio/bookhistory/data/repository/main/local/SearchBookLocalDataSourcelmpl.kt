package com.ejstudio.bookhistory.data.repository.main.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.data.db.RecentSearchesDao
import com.ejstudio.bookhistory.data.model.RecentSearchesEntity
import com.ejstudio.bookhistory.util.UserInfo
import io.reactivex.Completable
import io.reactivex.rxjava3.core.Observable

class SearchBookLocalDataSourcelmpl(
    private val recentSaechesDao: RecentSearchesDao
) : SearchBookLocalDataSource {

    override fun insertRecentSearches(recentSearches: RecentSearchesEntity): Completable {
        return recentSaechesDao.insertRecentSearches(recentSearches)
    }

    override fun getRecentSearchesList(email: String): LiveData<List<RecentSearchesEntity>> {
        return recentSaechesDao.getAllRecentSearches(email)
    }
}