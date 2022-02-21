package com.ejstudio.bookhistory.data.repository.main.setting

import android.util.Log
import com.ejstudio.bookhistory.data.repository.main.setting.local.SettingLocalDataSource
import com.ejstudio.bookhistory.data.repository.main.setting.remote.SettingRemoteDataSource
import com.ejstudio.bookhistory.domain.repository.SettingRepository
import io.reactivex.rxjava3.core.Single

class SettingRepositorylmpl(
    private val settingLocalDataSource: SettingLocalDataSource,
    private val settingRemoteDataSource: SettingRemoteDataSource
) : SettingRepository {
    override fun requestLogout(): Single<Boolean> {
        return settingLocalDataSource.requestLogout()
    }

    override fun removeUserAccount(email: String): Single<Boolean> {
        return settingRemoteDataSource.removeUserAccount(email)
            .flatMap { returnValue ->
                if (returnValue.returnvalue.toBoolean()) {
                    Single.just(true)
                } else {
                    Single.just(false)
                }
            }
    }
}