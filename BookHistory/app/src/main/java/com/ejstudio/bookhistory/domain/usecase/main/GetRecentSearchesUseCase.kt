package com.ejstudio.bookhistory.domain.usecase.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.data.model.RecentSearchesEntity
import com.ejstudio.bookhistory.domain.repository.BookSearchRepository

class GetRecentSearchesUseCase(
    private val bookSearchRepository: BookSearchRepository
) {

    fun execute(email: String) : LiveData<List<RecentSearchesEntity>> {
        return bookSearchRepository.getRecentSearchesList(email)
    }
}