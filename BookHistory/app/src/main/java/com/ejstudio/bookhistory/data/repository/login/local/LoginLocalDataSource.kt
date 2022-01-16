package com.ejstudio.bookhistory.data.repository.login.local

import io.reactivex.rxjava3.core.Observable

interface LoginLocalDataSource {
    var isFirstWelcome: Boolean
}