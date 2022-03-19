package com.ejstudio.bookhistory.presentation.view.viewmodel

import com.ejstudio.bookhistory.domain.usecase.IsFirstWelcomeUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel

class WelcomeViewModel(
    private val isFirstWelcomeUseCase: IsFirstWelcomeUseCase
) : BaseViewModel() {

    fun changeIsFirstWelcome() {
        isFirstWelcomeUseCase.execute(false)
    }
}