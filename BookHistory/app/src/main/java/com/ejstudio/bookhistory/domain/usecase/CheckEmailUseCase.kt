package com.ejstudio.bookhistory.domain.usecase

import com.ejstudio.bookhistory.domain.model.CheckEmailModel
import com.ejstudio.bookhistory.domain.repository.LoginRepository
import io.reactivex.rxjava3.core.Observable

class CheckEmailUseCase(
    private val loginRepository: LoginRepository
) {
    fun execute(email: String): Observable<CheckEmailModel> {

        return loginRepository.checkEmail(email)
    }
}