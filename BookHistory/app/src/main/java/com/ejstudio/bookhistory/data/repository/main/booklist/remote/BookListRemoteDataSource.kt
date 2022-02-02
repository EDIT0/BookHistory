package com.ejstudio.bookhistory.data.repository.main.booklist.remote

import com.ejstudio.bookhistory.domain.model.CheckTrueOrFalseModel
import io.reactivex.rxjava3.core.Single

interface BookListRemoteDataSource {
    fun deleteIdxBookInfo(email: String, idx: Int) : Single<CheckTrueOrFalseModel>
}