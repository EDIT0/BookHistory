package com.ejstudio.bookhistory.presentation.view.viewmodel.main

import android.os.Build
import android.util.Log
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.data.api.ApiClient
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.domain.model.RecentPopularBookModel
import com.ejstudio.bookhistory.domain.model.RecommendBookModel
import com.ejstudio.bookhistory.domain.model.TextImageMemoModel
import com.ejstudio.bookhistory.domain.usecase.login.GetProtectDuplicateLoginTokenFromServerUseCase
import com.ejstudio.bookhistory.domain.usecase.login.GetTotalUserInfoUseCase
import com.ejstudio.bookhistory.domain.usecase.login.InitRoomForCurrentUserUseCase
import com.ejstudio.bookhistory.domain.usecase.login.SendFindPasswordEmailUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booklist.GetBeforeReadBookUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booklist.GetEndReadBookUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booklist.GetReadingBookUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booksearch.GetAlwaysPopularBookUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booksearch.GetRecentPopularBookUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booksearch.GetRecommendBookUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booksearch.GetTotalBookUseCase
import com.ejstudio.bookhistory.domain.usecase.main.mybookhistory.GetEmailTotalTextImageMemoUseCase
import com.ejstudio.bookhistory.domain.usecase.main.setting.RemoveUserAccountUseCase
import com.ejstudio.bookhistory.domain.usecase.main.setting.RequestLogoutUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel
import com.ejstudio.bookhistory.presentation.view.fragment.main.SettingFragment
import com.ejstudio.bookhistory.util.Converter
import com.ejstudio.bookhistory.util.NetworkManager
import com.ejstudio.bookhistory.util.UserInfo
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.kakao.sdk.user.UserApiClient
import java.lang.Exception
import java.time.LocalDate


class MainViewModel(
    private val getTotalBookUseCase : GetTotalBookUseCase,
    private val getBeforeReadBookUseCase: GetBeforeReadBookUseCase,
    private val getReadingBookUseCase: GetReadingBookUseCase,
    private val getEndReadBookUseCase: GetEndReadBookUseCase,
    private val getRecentPopularBookUseCase: GetRecentPopularBookUseCase,
    private val getRecommendBookUseCase : GetRecommendBookUseCase,
    private val getAlwaysPopularBookUseCase : GetAlwaysPopularBookUseCase,
    private val getEmailTotalTextImageMemoUseCase : GetEmailTotalTextImageMemoUseCase,
    private val requestLogoutUseCase: RequestLogoutUseCase,
    private val getProtectDuplicateLoginTokenFromServerUseCase: GetProtectDuplicateLoginTokenFromServerUseCase,
    private val initRoomForCurrentUserUseCase: InitRoomForCurrentUserUseCase,
    private val getTotalUserInfoUseCase: GetTotalUserInfoUseCase,
    private val sendFindPasswordEmailUseCase: SendFindPasswordEmailUseCase,
    private val removeUserAccountUseCase: RemoveUserAccountUseCase,
    private val networkManager: NetworkManager
): BaseViewModel() {

    private val TAG = MainViewModel::class.java.simpleName

    var snackbarMessage = String()
    private val _requestSnackbar: MutableLiveData<Unit> = MutableLiveData()
    val requestSnackbar: LiveData<Unit> get() = _requestSnackbar

    public var _selectedMenu : MutableLiveData<String> = MutableLiveData()
    val selectedMenu: LiveData<String> get() = _selectedMenu

    private val _isLoadingRecentPopularBookList = MutableLiveData<Boolean>(false)
    val isLoadingRecentPopularBookList: LiveData<Boolean> get() = _isLoadingRecentPopularBookList

    private val _isLoadingAlwaysPopularBookList = MutableLiveData<Boolean>(false)
    val isLoadingAlwaysPopularBookList: LiveData<Boolean> get() = _isLoadingAlwaysPopularBookList

    private val _isLoadingRecommendBookList = MutableLiveData<Boolean>(false)
    val isLoadingRecommendBookList: LiveData<Boolean> get() = _isLoadingRecommendBookList

    var cal: Calendar = Calendar.getInstance()
    var date= Date()
    var todayDate = ""
    var before60days = ""
    var page = 1
    var pageSize = 10
    var calendarDate = MutableLiveData<String>()
    var pickerYear= MutableLiveData<String>() // ??? ????????? ??????
    @RequiresApi(Build.VERSION_CODES.O)
    val onlyDate: LocalDate = LocalDate.now()
    var calendarYear = MutableLiveData<String>()

    var adCount = 0;

    init {
        pickerYear.value = onlyDate.toString().substring(0,4)
        calendarYear.value = onlyDate.toString().substring(0,4)
        getDate(-60) // ??? ?????? ?????? 60??? ??? ?????? ?????????
        Log.i(TAG, "?????? ??? ?????? ??????!!!!!!!!!!!!!!!!!!!!!!! ${pickerYear.value.toString()}")
        Log.i(TAG, "?????? url:  ${ApiClient.BOOK_BASE_URL}loanItemSrch?authKey=${ApiClient.BOOK_API_KEY}&startDt=$before60days" +
                "&endDt=$todayDate&page=$page&pageSize=${pageSize}&format=${ApiClient.FORMAT_JSON}" )

        _selectedMenu.value = "?????? ???" // ????????? ??????
        getRecentPopularBookList() // ?????? ?????? ?????? ?????? ??????
        getAlwaysPopularBookList() // ????????? ???????????? ???

    }

    private fun getDate(day: Int) {
        cal.time = date

        cal.add(Calendar.DAY_OF_MONTH, 0)
        todayDate = SimpleDateFormat("yyyy-MM-dd").format(cal.time)
        cal.add(Calendar.DAY_OF_MONTH, day)
        before60days = SimpleDateFormat("yyyy-MM-dd").format(cal.time)

        Log.i(TAG, "?????? ?????? : " + before60days + " / " + todayDate)
    }

    fun getProtectDuplicateLoginToken(email: String, preferencesToken: String) {
        if (!checkNetworkState()) return
        compositeDisposable.add(
            getProtectDuplicateLoginTokenFromServerUseCase.execute(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .doAfterTerminate {
                    hideProgress()
                }
                .subscribe { it, exception ->
                    Log.i(TAG, "????????????: " + preferencesToken + " / ????????????: ${Converter.decByKey(Converter.key, it.returnvalue)}")
                    if(Converter.decByKey(Converter.key, it.returnvalue).equals(preferencesToken)) {
                        Log.i(TAG, "??????????????? ???????????? ????????? ??????: true")
                        _tokenTrue.value = Unit
                    } else {
                        Log.i(TAG, "??????????????? ???????????? ????????? ??????: false")
                        _tokenFalse.value = Unit
                    }
                }
        )
    }

    fun initRoomForCurrentUser(email: String) {
        if (!checkNetworkState()) return
        compositeDisposable.add(
            initRoomForCurrentUserUseCase.execute(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .doAfterTerminate {
                    hideProgress()
                }
                .subscribe(
                    {
                        Log.i(TAG, "?????? ???????????? ?????? Room ?????? ?????????")
                        _requestTotalUserInfo.value = Unit
                    }, {
                        Log.i(TAG, "?????? ???????????? ?????? Room ?????? ?????? ?????? ${it.message.toString()}")
                    }
                )
        )
    }

    // ????????? ?????? ?????? ???, ?????? ???????????? ??????????????? ????????????.
    fun getTotalUserInfo(email: String) {
//        compositeDisposable.add(
        if (!checkNetworkState()) return
        getTotalUserInfoUseCase.execute(email)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe { showProgress() }
//                .doAfterTerminate {
//                    hideProgress()
//                }
//                .subscribe(
//                    {
//                        Log.i(TAG, "?????? ???????????? ?????? Room ?????? ?????????")
//                        _requestTotalUserInfo.value = Unit
//                    }, {
//                        Log.i(TAG, "?????? ???????????? ?????? Room ?????? ?????? ?????? ${it.message.toString()}")
//                    }
//                )
//        )
    }

//    private fun get365Date(day: Int) : String {
//        cal.add(Calendar.DAY_OF_MONTH, day)
//        Log.i(TAG, "1??? ??? ??????: ${day} " + SimpleDateFormat("yyyy-MM-dd").format(cal.time))
//        return SimpleDateFormat("yyyy-MM-dd").format(cal.time)
//    }
//    var calendarDateMemoList = ArrayList<String>()
//    fun setViewPagerDateItem(day: Int, clickDate: Date) {
//        try {
//            calendarDateMemoList.clear()
//        }catch (e : Exception) {}
//
//        cal.time = clickDate // ????????? ?????????
//        val transFormat = SimpleDateFormat("yyyy-MM-dd")
//        val to: Date = transFormat.parse(get365Date(-day)) // ???????????? 1????????? ?????????
//        cal.time = to // ?????? ?????? ???????????? ?????????
//
//       for(i in 0 until day) {
//            cal.add(Calendar.DATE, 1)
//            calendarDateMemoList.add(SimpleDateFormat("yyyy-MM-dd").format(cal.time))
//            Log.i(TAG, "?????? ???????????????. ${i} ${SimpleDateFormat("yyyy-MM-dd").format(cal.time)}")
//        }
//
//        for(i in 0 until day) {
//            cal.add(Calendar.DATE, 1)
//            calendarDateMemoList.add(SimpleDateFormat("yyyy-MM-dd").format(cal.time))
//            Log.i(TAG, "?????? ???????????????. ${i} ${SimpleDateFormat("yyyy-MM-dd").format(cal.time)}")
//        }
//    }

    private val _logoutSuccess : MutableLiveData<Unit> = MutableLiveData()
    val logoutSuccess: LiveData<Unit> get() = _logoutSuccess

    private val _logoutFail : MutableLiveData<Unit> = MutableLiveData()
    val logoutFail: LiveData<Unit> get() = _logoutFail

    private val _tokenTrue : MutableLiveData<Unit> = MutableLiveData()
    val tokenTrue: LiveData<Unit> get() = _tokenTrue

    private val _tokenFalse : MutableLiveData<Unit> = MutableLiveData()
    val tokenFalse: LiveData<Unit> get() = _tokenFalse

    // ??? ?????? ?????? ??? ???????????? ?????? ?????? ?????? ???????????? ??????????????? ??? ????????? insert?????????.
    private val _requestTotalUserInfo : MutableLiveData<Unit> = MutableLiveData()
    val requestTotalUserInfo: LiveData<Unit> get() = _requestTotalUserInfo

    // ??? ?????? ?????????
    private val _totalBookList = getTotalBookUseCase.execute(UserInfo.email)
    val totalBookList: LiveData<List<BookListEntity>> get() = _totalBookList

    // ?????? ???
//    val beforeReadBookList: LiveData<List<BookListEntity>> = Transformations.switchMap(pickerYear) { param->
//        getBeforeReadBookUseCase.execute(UserInfo.email, BEFORE_READ)
//    }

    // ?????? ???
//    val readingBookList: LiveData<List<BookListEntity>> = Transformations.switchMap(pickerYear) { param->
//        getReadingBookUseCase.execute(UserInfo.email, READING)
//    }

    // ?????? ???
    val endReadBookList: LiveData<List<BookListEntity>> = Transformations.switchMap(pickerYear) { param->
        getEndReadBookUseCase.execute(UserInfo.email, END_READ, param)
    }

    fun searchByCategory(param: String) {
        pickerYear.value = param
    }

    // ?????? ???
    private val _beforeReadBookList = getBeforeReadBookUseCase.execute(UserInfo.email, BEFORE_READ)
    val beforeReadBookList: LiveData<List<BookListEntity>> get() = _beforeReadBookList

    // ?????? ???
    private val _readingBookList = getReadingBookUseCase.execute(UserInfo.email, READING)
    val readingBookList: LiveData<List<BookListEntity>> get() = _readingBookList

    // ?????? ???
//    private val _endReadBookList = getEndReadBookUseCase.execute(UserInfo.email, END_READ)
//    val endReadBookList: LiveData<List<BookListEntity>> get() = _endReadBookList

    // ?????? ?????? ?????? ??????
    private val _recentPopularBookList = ArrayList<RecentPopularBookModel.Response.Doc>()
    val recentPopularBookList = MutableLiveData<ArrayList<RecentPopularBookModel.Response.Doc>>()

    // ????????? ?????? ??????
    private val _recommendBookList = ArrayList<RecommendBookModel.Response.Doc>()
    val recommendBookList = MutableLiveData<ArrayList<RecommendBookModel.Response.Doc>>()

    // ????????? ???????????? ???
    private val _alwaysPopularBookList = ArrayList<RecentPopularBookModel.Response.Doc>()
    val alwaysPopularBookList = MutableLiveData<ArrayList<RecentPopularBookModel.Response.Doc>>()

    // ???, ????????? ?????? ?????????
    val totalTextImageMemoList: LiveData<List<TextImageMemoModel>> = Transformations.switchMap(calendarYear) { param->
        getEmailTotalTextImageMemoUseCase.execute(UserInfo.email, param)
    }
    // ???, ????????? ?????? ?????????
//    private val _totalTextImageMemoList = getEmailTotalTextImageMemoUseCase.execute(UserInfo.email)
//    val totalTextImageMemoList: LiveData<List<TextImageMemoModel>> get() = _totalTextImageMemoList




    // ???????????? ????????? ????????? ???, ????????? ?????? ?????????
//    private val _getDateTextMemoList = getEmailTotalTextImageMemoUseCase.execute(UserInfo.email, date)
//    val getDateTextMemoList: LiveData<List<TextMemoEntity>> get() = _getDateTextMemoList

    fun changeList(state: String) {
        when (state) {
            BEFORE_READ -> _selectedMenu.value = "?????? ???"
            READING -> _selectedMenu.value = "?????? ???"
            END_READ -> _selectedMenu.value = "?????? ???"
        }
    }

    fun getRecentPopularBookList() {
        if (!checkNetworkState()) return
        var _page = (1..10).random()
        compositeDisposable.add(
            getRecentPopularBookUseCase.execute(before60days, todayDate, _page, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _isLoadingRecentPopularBookList.value = true }
                .doAfterTerminate {
                    _isLoadingRecentPopularBookList.value = false
                }
                .subscribe({ it ->
                    if (it.response.resultNum == 0) {
                    } else {
                        Log.i(TAG, "?????? ?????? ?????? ??? ??????: " + it.response.docs.get(0).doc.bookname)
                        _recentPopularBookList.addAll(it.response.docs)
                        recentPopularBookList.value = _recentPopularBookList
                    }
                }, {
                    Log.i(TAG, "?????? ?????? ?????? ??? ?????? ??????: " + it.message.toString())
                })
        )
    }

    /*
    * ??????????????? ?????? ????????? ??????????????? ?????? ??? ??????
    * */
    var count = 0
    fun getRecommendBookList(isbn: String) {
        if (!checkNetworkState()) return
        Log.i(TAG, "????????? ?????? ?????? ?????? ?????? " + isbn)
        compositeDisposable.add(
            getRecommendBookUseCase.execute(isbn)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _isLoadingRecommendBookList.value = true }
                .doAfterTerminate {
                    _isLoadingRecommendBookList.value = false
                }
                .subscribe({ it ->
                    if (it.response.resultNum == 0) {
                        // TODO ?????? ????????? ????????? ??? ????????????????
                            if(count == 0) {
                                count++
                                getRecommendBookList("9791165341909;")
                            }
                    } else {
                        Log.i(TAG, "????????? ?????? ?????? ??????: " + it.response.docs.get(0).book.bookname)
                        _recommendBookList.addAll(it.response.docs)
                        recommendBookList.value = _recommendBookList
                    }
                }, {
                    Log.i(TAG, "????????? ?????? ?????? ?????? ??????: " + it.message.toString())
                })
        )
    }

    /*
    * ????????? ???????????? ???
    * */
    fun getAlwaysPopularBookList() {
        if (!checkNetworkState()) return
        var _page = (1..10).random()
        compositeDisposable.add(
            getAlwaysPopularBookUseCase.execute(_page, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _isLoadingAlwaysPopularBookList.value = true }
                .doAfterTerminate {
                    _isLoadingAlwaysPopularBookList.value = false
                }
                .subscribe({ it ->
                    if (it.response.resultNum == 0) {
                    } else {
                        Log.i(TAG, "???????????? ???????????? ??? ??????: " + it.response.docs.get(0).doc.bookname)
                        _alwaysPopularBookList.addAll(it.response.docs)
                        alwaysPopularBookList.value = _alwaysPopularBookList
                    }
                }, {
                    Log.i(TAG, it.message.toString())
                    getAlwaysPopularBookList()
                })
        )
    }

//    private val _goToSearch: MutableLiveData<Unit> = MutableLiveData()
//    val goToSearch: LiveData<Unit> get() = _goToSearch
//
//
//
//    fun goToSearch() {
//        _goToSearch.value = Unit
//    }

    fun accountLogout(type: String) {
        if (!checkNetworkState()) return
        compositeDisposable.add(
            requestLogoutUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .doAfterTerminate {
                    hideProgress()
                }
                .subscribe { it, exception ->
                    Log.i(TAG, "????????????: " + it)
                    if (it.toString().toBoolean()) {
                        Log.i(TAG, "???????????? ??????")

                        if(type.equals(SettingFragment.CHANGE_PASSWORD)) {
                            sendFindPasswordEmail()
                        } else if(type.equals(SettingFragment.LOGOUT)) {
                            // ?????????????????? ????????????
                            if(FirebaseAuth.getInstance().currentUser != null) {
                                Log.i(TAG, "?????????????????? ???????????? - ????????????")
                                FirebaseAuth.getInstance().signOut()
                            }
                            // ????????? ????????????
                            if(UserApiClient.instance != null) {
                                UserApiClient.instance.logout { error ->
                                    if (error != null) {
                                        Log.e(TAG, "???????????? ??????. SDK?????? ?????? ?????????", error)
                                    }
                                    else {
                                        Log.i(TAG, "???????????? ??????. SDK?????? ?????? ?????????")
                                    }
                                }
                            }
                            _logoutSuccess.value = Unit
                        } else if(type.equals(SettingFragment.REMOVE_ACCOUNT)) {
                            removeUserAccount()
                        }



                    } else {
                        Log.i(TAG, "???????????? ??????")
                        _logoutFail.value = Unit

                    }
                }
        )
    }

    fun sendFindPasswordEmail() {
        if (!checkNetworkState()) return
        Log.i(TAG, "(???????????? ??????) ?????? ????????? ??????: "+UserInfo.email)
        compositeDisposable.add(sendFindPasswordEmailUseCase.execute(UserInfo.email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showProgress() }
            .doAfterTerminate {
                hideProgress()
            }
            .subscribe({
                Log.i(TAG, "???????????? ?????????????: ${it.toString().toBoolean()}")
                if(it.toString().toBoolean()) {
                    _logoutSuccess.value = Unit
//                    _goToFindPassword2.value = Unit
                } else {
//                    snackbarMessage = "?????? ??????????????????."
//                    _requestSnackbar.value = Unit
                }
            }, {
                Log.i(TAG, "??????: $it ${it.message}")
//                snackbarMessage = "?????? ??????????????????."
//                _requestSnackbar.value = Unit
            })
        )
    }

    fun removeUserAccount() {
        if (!checkNetworkState()) return
        removeUserAccountUseCase.execute(UserInfo.email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showProgress() }
            .doAfterTerminate {
                hideProgress()
//                _backButton.value = Unit
            }
            .subscribe({
                Log.i(TAG, "?????? ?????? ??????")
                if(FirebaseAuth.getInstance().currentUser != null && UserInfo.email.contains("@")) {
                    FirebaseAuth.getInstance().currentUser?.delete()
                        ?.addOnCompleteListener(object : OnFailureListener,
                            OnCompleteListener<Void> {
                            override fun onFailure(p0: Exception) {
                                Log.i(TAG, "??????")
                                _logoutSuccess.value = Unit
                            }

                            override fun onComplete(p0: Task<Void>) {
                                Log.i(TAG, "??????")
                                _logoutSuccess.value = Unit
                            }

                        })
                } else if(UserApiClient.instance != null) {
                    UserApiClient.instance.unlink { throwable: Throwable? ->
                        if (throwable != null) {
                            // @brief : ?????? ?????? ??????
                            Log.e("[?????????] ????????????", "?????? ?????? ??????")
                            _logoutSuccess.value = Unit
                        } else {
                            // @brief : ?????? ?????? ??????
                            Log.i("kakaoLogout", "?????? ?????? ??????. SDK?????? ?????? ??????")
                            _logoutSuccess.value = Unit
                        }
                    }
                } else {
                    _logoutSuccess.value = Unit
                }

            }, {
                Log.i(TAG, "?????? ?????? ?????? " + it.message.toString())
            })
    }

        fun checkNetworkState(): Boolean {
        return if (networkManager.checkNetworkState()) {
            true
        } else {
            snackbarMessage = MessageSet.NETWORK_NOT_CONNECTED.toString()
            _requestSnackbar.value = Unit
            false
        }
    }

    enum class MessageSet {
        NETWORK_NOT_CONNECTED
    }

    companion object {
        const val BEFORE_READ = "BEFORE_READ"
        const val READING = "READING"
        const val END_READ = "END_READ"
    }
}