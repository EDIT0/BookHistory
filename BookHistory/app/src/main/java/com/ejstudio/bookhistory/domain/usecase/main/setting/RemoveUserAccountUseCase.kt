package com.ejstudio.bookhistory.domain.usecase.main.setting

import com.ejstudio.bookhistory.domain.repository.SettingRepository
import io.reactivex.rxjava3.core.Single

class RemoveUserAccountUseCase(
    private val settingRepository: SettingRepository
) {
    fun execute(email: String) : Single<Boolean> {
        return settingRepository.removeUserAccount(email)
    }
}