package com.ejstudio.bookhistory.data.repository.main.setting.remote

import com.ejstudio.bookhistory.data.api.ApiInterface
import com.ejstudio.bookhistory.domain.model.CheckTrueOrFalseModel
import io.reactivex.rxjava3.core.Single

class SettingRemoteDataSourcelmpl(
    private val apiInterface: ApiInterface
) : SettingRemoteDataSource {
    override fun removeUserAccount(email: String): Single<CheckTrueOrFalseModel> {
        return apiInterface.removeUserAccount(email)
    }
}