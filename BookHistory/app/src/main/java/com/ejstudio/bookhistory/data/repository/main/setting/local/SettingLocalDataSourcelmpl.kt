package com.ejstudio.bookhistory.data.repository.main.setting.local

import com.ejstudio.bookhistory.util.PreferenceManager
import io.reactivex.rxjava3.core.Single

class SettingLocalDataSourcelmpl(
    private val preferenceManager: PreferenceManager
) : SettingLocalDataSource {
    override fun requestLogout(): Single<Boolean> {
        return preferenceManager.requestLogout()
    }
}