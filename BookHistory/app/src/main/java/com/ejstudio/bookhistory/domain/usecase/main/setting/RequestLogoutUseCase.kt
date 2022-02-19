package com.ejstudio.bookhistory.domain.usecase.main.setting

import com.ejstudio.bookhistory.domain.repository.SettingRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class RequestLogoutUseCase(
    private val settingRepository: SettingRepository
) {
    fun execute() : Single<Boolean> {
        return settingRepository.requestLogout()
    }
}