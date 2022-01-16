package com.ejstudio.bookhistory.domain.usecase

import com.ejstudio.bookhistory.domain.repository.LoginRepository
import io.reactivex.rxjava3.core.Observable

class SendSignUpEmailUseCase(
    private val loginRepository: LoginRepository
) {
    fun execute(email: String, randomNumber: String): Observable<String> {

        return loginRepository.sendEmail(email, randomNumber)
    }
}