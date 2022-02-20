package com.ejstudio.bookhistory.domain.usecase.login

import com.ejstudio.bookhistory.domain.repository.LoginRepository
import io.reactivex.rxjava3.core.Completable

class InitRoomForCurrentUserUseCase(
    private val loginRepository: LoginRepository
) {
    fun execute(email: String) : Completable {
        return loginRepository.initRoomForCurrentUser(email)
    }
}