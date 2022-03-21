package com.ejstudio.bookhistory.presentation.view.viewmodel.main

import android.util.Log
import androidx.annotation.Nullable
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

    var cal: Calendar = Calendar.getInstance()
    var date= Date()
    var todayDate = ""
    var before60days = ""
    var page = 1
    var pageSize = 10
    var calendarDate = MutableLiveData<String>()
    var pickerYear= MutableLiveData<String>() // 책 리스트 연도
    val onlyDate: LocalDate = LocalDate.now()
    var calendarYear = MutableLiveData<String>()

    var adCount = 0;

    init {
        pickerYear.value = onlyDate.toString().substring(0,4)
        calendarYear.value = onlyDate.toString().substring(0,4)
        getDate(-60) // 현 시간 기준 60일 전 날짜 구하기
        Log.i(TAG, "메인 뷰 모델 호출!!!!!!!!!!!!!!!!!!!!!!! ${pickerYear.value.toString()}")
        Log.i(TAG, "요청 url:  ${ApiClient.BOOK_BASE_URL}loanItemSrch?authKey=${ApiClient.BOOK_API_KEY}&startDt=$before60days" +
                "&endDt=$todayDate&page=$page&pageSize=${pageSize}&format=${ApiClient.FORMAT_JSON}" )

        _selectedMenu.value = "읽는 중" // 스타트 메뉴
        getRecentPopularBookList() // 요즘 많이 읽는 책들 호출
        getAlwaysPopularBookList() // 언제나 인기있는 책

    }

    private fun getDate(day: Int) {
        cal.time = date

        cal.add(Calendar.DAY_OF_MONTH, 0)
        todayDate = SimpleDateFormat("yyyy-MM-dd").format(cal.time)
        cal.add(Calendar.DAY_OF_MONTH, day)
        before60days = SimpleDateFormat("yyyy-MM-dd").format(cal.time)

        Log.i(TAG, "날짜 찍기 : " + before60days + " / " + todayDate)
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
                    Log.i(TAG, "내부토큰: " + preferencesToken + " / 서버토큰: ${Converter.decByKey(Converter.key, it.returnvalue)}")
                    if(Converter.decByKey(Converter.key, it.returnvalue).equals(preferencesToken)) {
                        Log.i(TAG, "내부토큰과 서버토큰 같은지 비교: true")
                        _tokenTrue.value = Unit
                    } else {
                        Log.i(TAG, "내부토큰과 서버토큰 같은지 비교: false")
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
                        Log.i(TAG, "현재 로그인한 유저 Room 정보 삭제됌")
                        _requestTotalUserInfo.value = Unit
                    }, {
                        Log.i(TAG, "현재 로그인한 유저 Room 정보 삭제 실패 ${it.message.toString()}")
                    }
                )
        )
    }

    // 유저에 대한 모든 책, 메모 정보들을 서버로부터 불러온다.
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
//                        Log.i(TAG, "현재 로그인한 유저 Room 정보 삭제됌")
//                        _requestTotalUserInfo.value = Unit
//                    }, {
//                        Log.i(TAG, "현재 로그인한 유저 Room 정보 삭제 실패 ${it.message.toString()}")
//                    }
//                )
//        )
    }

//    private fun get365Date(day: Int) : String {
//        cal.add(Calendar.DAY_OF_MONTH, day)
//        Log.i(TAG, "1년 전 날짜: ${day} " + SimpleDateFormat("yyyy-MM-dd").format(cal.time))
//        return SimpleDateFormat("yyyy-MM-dd").format(cal.time)
//    }
//    var calendarDateMemoList = ArrayList<String>()
//    fun setViewPagerDateItem(day: Int, clickDate: Date) {
//        try {
//            calendarDateMemoList.clear()
//        }catch (e : Exception) {}
//
//        cal.time = clickDate // 기준일 정하고
//        val transFormat = SimpleDateFormat("yyyy-MM-dd")
//        val to: Date = transFormat.parse(get365Date(-day)) // 기준일에 1년전을 구하고
//        cal.time = to // 구한 값을 기준으로 설정정
//
//       for(i in 0 until day) {
//            cal.add(Calendar.DATE, 1)
//            calendarDateMemoList.add(SimpleDateFormat("yyyy-MM-dd").format(cal.time))
//            Log.i(TAG, "숫자 올라갑니다. ${i} ${SimpleDateFormat("yyyy-MM-dd").format(cal.time)}")
//        }
//
//        for(i in 0 until day) {
//            cal.add(Calendar.DATE, 1)
//            calendarDateMemoList.add(SimpleDateFormat("yyyy-MM-dd").format(cal.time))
//            Log.i(TAG, "숫자 올라갑니다. ${i} ${SimpleDateFormat("yyyy-MM-dd").format(cal.time)}")
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

    // 룸 디비 삭제 후 서버에서 모든 책과 메모 정보들을 가지고와서 룸 디비에 insert시킨다.
    private val _requestTotalUserInfo : MutableLiveData<Unit> = MutableLiveData()
    val requestTotalUserInfo: LiveData<Unit> get() = _requestTotalUserInfo

    // 책 전체 리스트
    private val _totalBookList = getTotalBookUseCase.execute(UserInfo.email)
    val totalBookList: LiveData<List<BookListEntity>> get() = _totalBookList

    // 읽기 전
    val beforeReadBookList: LiveData<List<BookListEntity>> = Transformations.switchMap(pickerYear) { param->
        getBeforeReadBookUseCase.execute(UserInfo.email, BEFORE_READ, param)
    }

    // 읽는 중
    val readingBookList: LiveData<List<BookListEntity>> = Transformations.switchMap(pickerYear) { param->
        getReadingBookUseCase.execute(UserInfo.email, READING, param)
    }

    // 읽은 후
    val endReadBookList: LiveData<List<BookListEntity>> = Transformations.switchMap(pickerYear) { param->
        getEndReadBookUseCase.execute(UserInfo.email, END_READ, param)
    }

    fun searchByCategory(param: String) {
        pickerYear.value = param
    }

    // 읽기 전
//    private val _beforeReadBookList = getBeforeReadBookUseCase.execute(UserInfo.email, BEFORE_READ, pickerYear.value.toString())
//    val beforeReadBookList: LiveData<List<BookListEntity>> get() = _beforeReadBookList

    // 읽는 중
//    private val _readingBookList = getReadingBookUseCase.execute(UserInfo.email, READING, pickerYear.value.toString())
//    val readingBookList: LiveData<List<BookListEntity>> get() = _readingBookList

    // 읽은 후
//    private val _endReadBookList = getEndReadBookUseCase.execute(UserInfo.email, END_READ)
//    val endReadBookList: LiveData<List<BookListEntity>> get() = _endReadBookList

    // 요즘 많이 읽는 책들
    private val _recentPopularBookList = ArrayList<RecentPopularBookModel.Response.Doc>()
    val recentPopularBookList = MutableLiveData<ArrayList<RecentPopularBookModel.Response.Doc>>()

    // 당신을 위한 추천
    private val _recommendBookList = ArrayList<RecommendBookModel.Response.Doc>()
    val recommendBookList = MutableLiveData<ArrayList<RecommendBookModel.Response.Doc>>()

    // 언제나 인기있는 책
    private val _alwaysPopularBookList = ArrayList<RecentPopularBookModel.Response.Doc>()
    val alwaysPopularBookList = MutableLiveData<ArrayList<RecentPopularBookModel.Response.Doc>>()

    // 글, 이미지 메모 리스트
    val totalTextImageMemoList: LiveData<List<TextImageMemoModel>> = Transformations.switchMap(calendarYear) { param->
        getEmailTotalTextImageMemoUseCase.execute(UserInfo.email, param)
    }
    // 글, 이미지 메모 리스트
//    private val _totalTextImageMemoList = getEmailTotalTextImageMemoUseCase.execute(UserInfo.email)
//    val totalTextImageMemoList: LiveData<List<TextImageMemoModel>> get() = _totalTextImageMemoList




    // 달력에서 선택한 날짜의 글, 이미지 메모 리스트
//    private val _getDateTextMemoList = getEmailTotalTextImageMemoUseCase.execute(UserInfo.email, date)
//    val getDateTextMemoList: LiveData<List<TextMemoEntity>> get() = _getDateTextMemoList

    fun changeList(state: String) {
        when (state) {
            BEFORE_READ -> _selectedMenu.value = "읽을 책"
            READING -> _selectedMenu.value = "읽는 중"
            END_READ -> _selectedMenu.value = "읽은 책"
        }
    }

    fun getRecentPopularBookList() {
        if (!checkNetworkState()) return
        var _page = (1..10).random()
        compositeDisposable.add(
            getRecentPopularBookUseCase.execute(before60days, todayDate, _page, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .doAfterTerminate {
                    hideProgress()
                }
                .subscribe({ it ->
                    if (it.response.resultNum == 0) {
                    } else {
                        Log.i(TAG, "요즘 많이 읽는 책 응답: " + it.response.docs.get(0).doc.bookname)
                        _recentPopularBookList.addAll(it.response.docs)
                        recentPopularBookList.value = _recentPopularBookList
                    }
                }, {
                    Log.i(TAG, "요즘 많이 읽는 책 응답 에러: " + it.message.toString())
                })
        )
    }

    /*
    * 가지고있는 전체 책들을 가지고와서 추천 책 요청
    * */
    var count = 0
    fun getRecommendBookList(isbn: String) {
        if (!checkNetworkState()) return
        Log.i(TAG, "당신을 위한 추천 응답 요청 " + isbn)
        compositeDisposable.add(
            getRecommendBookUseCase.execute(isbn)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .doAfterTerminate {
                    hideProgress()
                }
                .subscribe({ it ->
                    if (it.response.resultNum == 0) {
                        // TODO 응답 요청이 없으면 뭘 추천해주지?
                            if(count == 0) {
                                count++
                                getRecommendBookList("9791165341909;")
                            }
                    } else {
                        Log.i(TAG, "당신을 위한 추천 응답: " + it.response.docs.get(0).book.bookname)
                        _recommendBookList.addAll(it.response.docs)
                        recommendBookList.value = _recommendBookList
                    }
                }, {
                    Log.i(TAG, "당신을 위한 추천 응답 오류: " + it.message.toString())
                })
        )
    }

    /*
    * 언제나 인기있는 책
    * */
    fun getAlwaysPopularBookList() {
        if (!checkNetworkState()) return
        var _page = (1..10).random()
        compositeDisposable.add(
            getAlwaysPopularBookUseCase.execute(_page, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .doAfterTerminate {
                    hideProgress()
                }
                .subscribe({ it ->
                    if (it.response.resultNum == 0) {
                    } else {
                        Log.i(TAG, "오랫동안 인기있는 책 응답: " + it.response.docs.get(0).doc.bookname)
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
                    Log.i(TAG, "로그아웃: " + it)
                    if (it.toString().toBoolean()) {
                        Log.i(TAG, "로그아웃 성공")

                        if(type.equals(SettingFragment.CHANGE_PASSWORD)) {
                            sendFindPasswordEmail()
                        } else if(type.equals(SettingFragment.LOGOUT)) {
                            // 파이어베이스 로그아웃
                            if(FirebaseAuth.getInstance().currentUser != null) {
                                Log.i(TAG, "파이어베이스 로그아웃 - 로그아웃")
                                FirebaseAuth.getInstance().signOut()
                            }
                            // 카카오 로그아웃
                            if(UserApiClient.instance != null) {
                                UserApiClient.instance.logout { error ->
                                    if (error != null) {
                                        Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                                    }
                                    else {
                                        Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                                    }
                                }
                            }
                            _logoutSuccess.value = Unit
                        } else if(type.equals(SettingFragment.REMOVE_ACCOUNT)) {
                            removeUserAccount()
                        }



                    } else {
                        Log.i(TAG, "로그아웃 실패")
                        _logoutFail.value = Unit

                    }
                }
        )
    }

    fun sendFindPasswordEmail() {
        if (!checkNetworkState()) return
        Log.i(TAG, "(비밀번호 찾기) 보낼 이메일 주소: "+UserInfo.email)
        compositeDisposable.add(sendFindPasswordEmailUseCase.execute(UserInfo.email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showProgress() }
            .doAfterTerminate {
                hideProgress()
            }
            .subscribe({
                Log.i(TAG, "이메일을 보냈는가?: ${it.toString().toBoolean()}")
                if(it.toString().toBoolean()) {
                    _logoutSuccess.value = Unit
//                    _goToFindPassword2.value = Unit
                } else {
//                    snackbarMessage = "다시 시도해주세요."
//                    _requestSnackbar.value = Unit
                }
            }, {
                Log.i(TAG, "에러: $it ${it.message}")
//                snackbarMessage = "다시 시도해주세요."
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
                Log.i(TAG, "유저 삭제 성공")
                if(FirebaseAuth.getInstance().currentUser != null && UserInfo.email.contains("@")) {
                    FirebaseAuth.getInstance().currentUser?.delete()
                        ?.addOnCompleteListener(object : OnFailureListener,
                            OnCompleteListener<Void> {
                            override fun onFailure(p0: Exception) {
                                Log.i(TAG, "실패")
                                _logoutSuccess.value = Unit
                            }

                            override fun onComplete(p0: Task<Void>) {
                                Log.i(TAG, "성공")
                                _logoutSuccess.value = Unit
                            }

                        })
                } else if(UserApiClient.instance != null) {
                    UserApiClient.instance.unlink { throwable: Throwable? ->
                        if (throwable != null) {
                            // @brief : 연결 끊기 실패
                            Log.e("[카카오] 로그아웃", "연결 끊기 실패")
                            _logoutSuccess.value = Unit
                        } else {
                            // @brief : 연결 끊기 성공
                            Log.i("kakaoLogout", "연결 끊기 성공. SDK에서 토큰 삭제")
                            _logoutSuccess.value = Unit
                        }
                    }
                } else {
                    _logoutSuccess.value = Unit
                }

            }, {
                Log.i(TAG, "유저 삭제 오류 " + it.message.toString())
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