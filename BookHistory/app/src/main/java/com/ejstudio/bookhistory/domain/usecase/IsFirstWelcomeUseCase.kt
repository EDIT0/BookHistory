package com.ejstudio.bookhistory.domain.usecase

import com.ejstudio.bookhistory.domain.repository.LoginRepository

class IsFirstWelcomeUseCase(
    private val loginRepository: LoginRepository
) {
    fun execute() : Boolean = loginRepository.isFirstWelcome

    fun execute(isFirstWelcome: Boolean) {
        loginRepository.isFirstWelcome = isFirstWelcome
    }
}