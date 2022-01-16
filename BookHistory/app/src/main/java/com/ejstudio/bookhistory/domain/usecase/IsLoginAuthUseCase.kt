package com.ejstudio.bookhistory.domain.usecase

import com.ejstudio.bookhistory.domain.repository.LoginRepository
import io.reactivex.rxjava3.core.Observable

class IsLoginAuthUseCase(
    private val loginRepository: LoginRepository
) {
    fun execute(email: String, password: String): Observable<Boolean> {
//        loginRepository.email = email
//        loginRepository.password = password
//
//        return loginRepository.isLoginAuth
        return loginRepository.loginAuth(email, password)
    }
}