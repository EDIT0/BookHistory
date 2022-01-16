package com.ejstudio.bookhistory.domain.usecase

import com.ejstudio.bookhistory.domain.repository.LoginRepository
import io.reactivex.rxjava3.core.Observable

class CreateEmailUserUseCase(
    private val loginRepository: LoginRepository
) {
    fun execute(email: String, password: String) : Observable<Boolean> {
        return loginRepository.createEmailUser(email, password)
    }
}