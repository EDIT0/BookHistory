package com.ejstudio.bookhistory.data.repository.main.setting.local

import io.reactivex.rxjava3.core.Single

interface SettingLocalDataSource {
    fun requestLogout() : Single<Boolean>
}