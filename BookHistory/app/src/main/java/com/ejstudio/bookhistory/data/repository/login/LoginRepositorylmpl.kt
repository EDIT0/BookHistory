package com.ejstudio.bookhistory.data.repository.login

import android.util.Log
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.data.repository.login.local.LoginLocalDataSource
import com.ejstudio.bookhistory.data.repository.login.remote.LoginRemoteDataSource
import com.ejstudio.bookhistory.domain.model.CheckTrueOrFalseModel
import com.ejstudio.bookhistory.domain.repository.LoginRepository
import com.ejstudio.bookhistory.util.Converter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.Exception

class LoginRepositorylmpl(
    private val loginLocalDataSource: LoginLocalDataSource,
    private val loginRemoteDataSource: LoginRemoteDataSource
) : LoginRepository {

    private val TAG: String? = LoginRepositorylmpl::class.java.simpleName

    override var isFirstWelcome: Boolean
        get() = loginLocalDataSource.isFirstWelcome
        set(value) {
            loginLocalDataSource.isFirstWelcome = value
        }

    override var isAutoLogin: Observable<Boolean>
        get() = loginRemoteDataSource.isAutoLogin
        set(value) {}


    override fun loginAuth(email: String, password: String): Observable<Boolean> {
        val currentTime: Long = System.currentTimeMillis()
        val protectDuplicateLoginToken = email + currentTime

        return loginRemoteDataSource.loginAuth(email, password, protectDuplicateLoginToken)
            .flatMap {
                if(it) {
                    loginRemoteDataSource.updateProtectDuplicateLoginToken(email, protectDuplicateLoginToken)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { t1, t2 ->
                            Log.i(TAG, "중복로그인 방지 로큰 저장: ${t1.returnvalue}")
                        }
                    Observable.just(it)
                } else {
                    Observable.just(it)
                }
            }
    }

    override fun sendEmail(email: String, randomNumber: String): Observable<String> {
        return loginRemoteDataSource.sendEmail(email, randomNumber)
    }

    override fun createEmailUser(email: String, password: String, protectDuplicateLoginToken: String): Observable<Boolean> {
        return loginRemoteDataSource.createEmailUser(email, password, protectDuplicateLoginToken)
    }

    override fun checkEmail(email: String): Single<CheckTrueOrFalseModel> {
        return loginRemoteDataSource.checkEmail(email)
    }

    override fun registerEmailAndPassword(email: String, password: String, protectDuplicateLoginToken: String): Observable<Unit> {
        return loginRemoteDataSource.registerEmailAndPassword(email, password, protectDuplicateLoginToken)
    }

    override fun sendFindPasswordEmail(email: String): Observable<Boolean> {
        return loginRemoteDataSource.sendFindPasswordEmail(email)
    }

    override fun updateProtectDuplicateLoginToken(userId: String, protectDuplicateLoginToken: String) {
        loginRemoteDataSource.updateProtectDuplicateLoginToken(userId, protectDuplicateLoginToken)
            .subscribe { t1, t2 ->
                Log.i(TAG, "카카오 중복로그인 방지 토큰: ${t1.returnvalue}")
            }
    }

    override fun getProtectDuplicateLoginTokenFromServer(email: String): Single<CheckTrueOrFalseModel> {
        return loginRemoteDataSource.getProtectDuplicateLoginTokenFromServer(email)
    }

    override fun initRoomForCurrentUser(email: String): Completable {
        return loginLocalDataSource.initRoomForCurrentUser(email)
    }

    override fun getTotalUserInfo(email: String) {
        // (동기화) 책 리스트 적용
        loginRemoteDataSource.getEmailTotalBookList(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it, exception ->
                try {
                    // 이메일 복호화
                    for(i in 0 until it.totalBookListModel.size) {
                        it.totalBookListModel.get(i).email = Converter.decByKey(Converter.key, it.totalBookListModel.get(i).email)!!
                    }
                    Log.i(TAG, "서버에서 온 전체 책 리스트 ${it}")
                }catch (e: Exception){}
                if(it.totalBookListModel != null && it.totalBookListModel.size != 0) {
                    loginLocalDataSource.insertTotalBookList(it.totalBookListModel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Log.i(TAG, "서버에서 온 전체 책 리스트 룸에 적용 완료")

                            // (동기화) 글 메모 리스트 적용
                            loginRemoteDataSource.getEmailTotalBookTextMemoList(email)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe { it, exception ->
                                    try {
                                        // 이메일 복호화
                                        for (i in 0 until it.totalBookTextMemoListModel.size) {
                                            it.totalBookTextMemoListModel.get(i).email = Converter.decByKey(Converter.key, it.totalBookTextMemoListModel.get(i).email)!!
                                        }
                                    }catch (e : Exception) {}
                                    if(it.totalBookTextMemoListModel != null && it.totalBookTextMemoListModel.size != 0) {
                                        loginLocalDataSource.insertTotalBookTextMemoList(it.totalBookTextMemoListModel)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe({
                                                Log.i(TAG, "서버에서 온 전체 책 리스트의 텍스트 메모 룸에 적용 완료")
                                            }, {
                                                Log.i(TAG, "서버에서 온 전체 책 리스트의 텍스트 메모 룸에 적용 실패 ${it.message.toString()}")
                                            })
                                    }
                                }

                            // (동기화) 사진 메모 리스트 적용
                            loginRemoteDataSource.getEmailTotalBookImageMemoList(email)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe { it, exception ->
                                    // 이메일 복호화
                                    try {
                                        for(i in 0 until it.totalBookImageMemoListModel.size) {
                                            it.totalBookImageMemoListModel.get(i).email = Converter.decByKey(Converter.key, it.totalBookImageMemoListModel.get(i).email)!!
                                        }
                                    }catch (e : Exception) {}

                                    if(it.totalBookImageMemoListModel != null && it.totalBookImageMemoListModel.size != 0) {
                                        loginLocalDataSource.insertTotalBookImageMemoList(it.totalBookImageMemoListModel)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe({
                                                Log.i(TAG, "서버에서 온 전체 책 리스트의 이미지 메모 룸에 적용 완료")
                                            }, {
                                                Log.i(TAG, "서버에서 온 전체 책 리스트의 이미지 메모 룸에 적용 실패 ${it.message.toString()}")
                                            })
                                    }
                                }
                        }, {
                            Log.i(TAG, "서버에서 온 전체 책 리스트 룸에 적용 실패 ${it.message.toString()}")
                        })
                }
            }
    }
}