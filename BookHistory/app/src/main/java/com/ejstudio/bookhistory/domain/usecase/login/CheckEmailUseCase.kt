package com.ejstudio.bookhistory.domain.usecase.login

import com.ejstudio.bookhistory.domain.model.CheckTrueOrFalseModel
import com.ejstudio.bookhistory.domain.repository.LoginRepository
import io.reactivex.rxjava3.core.Single

class CheckEmailUseCase(
    private val loginRepository: LoginRepository
) {
    fun execute(email: String): Single<CheckTrueOrFalseModel> {

        return loginRepository.checkEmail(email)
    }
}