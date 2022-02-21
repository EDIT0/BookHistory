package com.ejstudio.bookhistory.domain.repository

import io.reactivex.rxjava3.core.Single

interface SettingRepository {
    fun requestLogout() : Single<Boolean>
    fun removeUserAccount(email: String) : Single<Boolean>
}