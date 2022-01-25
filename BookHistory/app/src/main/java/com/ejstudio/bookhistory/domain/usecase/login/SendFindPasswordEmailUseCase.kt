package com.ejstudio.bookhistory.domain.usecase.login

import com.ejstudio.bookhistory.domain.repository.LoginRepository
import io.reactivex.rxjava3.core.Observable

class SendFindPasswordEmailUseCase(
    private val loginRepository: LoginRepository
) {
    fun execute(email: String) : Observable<Boolean> {
        return loginRepository.sendFindPasswordEmail(email)
    }
}