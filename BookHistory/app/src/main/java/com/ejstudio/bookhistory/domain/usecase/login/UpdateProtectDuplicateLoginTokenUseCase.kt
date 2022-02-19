package com.ejstudio.bookhistory.domain.usecase.login

import com.ejstudio.bookhistory.domain.repository.LoginRepository

class UpdateProtectDuplicateLoginTokenUseCase(
    private val loginRepository: LoginRepository
) {
    fun execute(userId: String, protectDuplicateLoginToken: String) {
        return loginRepository.updateProtectDuplicateLoginToken(userId, protectDuplicateLoginToken)
    }
}