package com.ejstudio.bookhistory.domain.usecase

import com.ejstudio.bookhistory.domain.model.CheckEmailModel
import com.ejstudio.bookhistory.domain.repository.LoginRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class CheckEmailUseCase(
    private val loginRepository: LoginRepository
) {
    fun execute(email: String): Single<CheckEmailModel> {

        return loginRepository.checkEmail(email)
    }
}