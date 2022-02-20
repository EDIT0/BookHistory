package com.ejstudio.bookhistory.domain.usecase.login

import com.ejstudio.bookhistory.domain.repository.LoginRepository
import io.reactivex.rxjava3.core.Completable

class GetTotalUserInfoUseCase(
    private val loginRepository: LoginRepository
) {
    fun execute(email: String) {
        loginRepository.getTotalUserInfo(email)
    }
}