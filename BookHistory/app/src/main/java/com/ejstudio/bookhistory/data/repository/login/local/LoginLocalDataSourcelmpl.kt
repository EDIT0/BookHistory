package com.ejstudio.bookhistory.data.repository.login.local

import com.ejstudio.bookhistory.util.PreferenceManager
import io.reactivex.rxjava3.core.Observable

class LoginLocalDataSourcelmpl(
    private val preferenceManager: PreferenceManager
) : LoginLocalDataSource {

    override var isFirstWelcome: Boolean
        get() = preferenceManager.isFirstWelcome
        set(value) {
            preferenceManager.isFirstWelcome = value
        }

}