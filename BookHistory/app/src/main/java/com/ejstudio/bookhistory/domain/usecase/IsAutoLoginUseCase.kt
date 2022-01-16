package com.ejstudio.bookhistory.domain.usecase

import com.ejstudio.bookhistory.domain.repository.LoginRepository
import io.reactivex.rxjava3.core.Observable

class IsAutoLoginUseCase(
    private val repository: LoginRepository
) {
    fun execute() : Observable<Boolean> = repository.isAutoLogin
}