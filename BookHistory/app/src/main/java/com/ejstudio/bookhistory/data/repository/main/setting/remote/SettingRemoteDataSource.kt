package com.ejstudio.bookhistory.data.repository.main.setting.remote

import com.ejstudio.bookhistory.domain.model.CheckTrueOrFalseModel
import io.reactivex.rxjava3.core.Single

interface SettingRemoteDataSource {
    fun removeUserAccount(email: String) : Single<CheckTrueOrFalseModel>
}