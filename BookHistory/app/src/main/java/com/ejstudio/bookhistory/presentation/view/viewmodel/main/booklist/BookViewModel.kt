package com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.data.model.ImageMemoEntity
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import com.ejstudio.bookhistory.domain.usecase.main.booklist.*
import com.ejstudio.bookhistory.presentation.base.BaseViewModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.MainViewModel
import com.ejstudio.bookhistory.util.ImageSenderModule
import com.ejstudio.bookhistory.util.NetworkManager
import com.ejstudio.bookhistory.util.UserInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class BookViewModel(
    private val getIdxBookInfoUseCase: GetIdxBookInfoUseCase,
    private val deleteIdxBookInfoUseCase: DeleteIdxBookInfoUseCase,
    private val updateBookReadingStateUseCase: UpdateBookReadingStateUseCase,
    private val getTextMemoUseCase: GetTextMemoUseCase,
    private val getImageMemoUseCase: GetImageMemoUseCase,
    private val insertImageMemo: InsertImageMemoUseCase,
    private val networkManager: NetworkManager
) : BaseViewModel() {

    private val TAG = BookViewModel::class.java.simpleName

    var snackbarMessage = String()
    private val _requestSnackbar: MutableLiveData<Unit> = MutableLiveData()
    val requestSnackbar: LiveData<Unit> get() = _requestSnackbar

    private val _backButton: MutableLiveData<Unit> = MutableLiveData()
    val backButton: LiveData<Unit> get() = _backButton

    private val _deleteBook: MutableLiveData<Unit> = MutableLiveData()
    val deleteBook: LiveData<Unit> get() = _deleteBook

    private val _showBookInfo: MutableLiveData<Unit> = MutableLiveData()
    val showBookInfo: LiveData<Unit> get() = _showBookInfo

    private val _contentsSeeDetail: MutableLiveData<Unit> = MutableLiveData()
    val contentsSeeDetail: LiveData<Unit> get() = _contentsSeeDetail

    private val _changeMenu: MutableLiveData<Unit> = MutableLiveData()
    val changeMenu: LiveData<Unit> get() = _changeMenu

    public var _selectedMenu : MutableLiveData<String> = MutableLiveData()
    val selectedMenu: LiveData<String> get() = _selectedMenu

    private val _clickFloaing: MutableLiveData<Unit> = MutableLiveData()
    val clickFloaing: LiveData<Unit> get() = _clickFloaing

    private val _goToMap: MutableLiveData<Unit> = MutableLiveData()
    val goToMap: LiveData<Unit> get() = _goToMap

    var currentTab = "TEXT" // 0번은 텍스트

    var book_idx = 0
    var reading_state = ""

    var bookThumbnail = String()
    var bookTitle = MutableLiveData<String>()
    var bookAuthors = MutableLiveData<String>()
    var bookPublisher = String()
    var bookDatetime = String()
    var bookContents = String()
    var bookUrl = String()
    var bookReadingState = String()
    var bookISBN = String()

    lateinit var _bookInfo: LiveData<BookListEntity>
    val bookInfo: LiveData<BookListEntity> get() = _bookInfo

    // 책 텍스트 메모 리스트
//    lateinit var _textMemoList: LiveData<List<TextMemoEntity>>
    val textMemoList: LiveData<List<TextMemoEntity>> get() = _textMemoList
    val _textMemoList: LiveData<List<TextMemoEntity>> by lazy {
        getTextMemoUseCase.execute(book_idx)
    }

    // 책 이미지 메모 리스트
    lateinit var _imageMemoList: LiveData<List<ImageMemoEntity>>
    val imageMemoList: LiveData<List<ImageMemoEntity>> get() = _imageMemoList
//    val _imageMemoList: LiveData<List<ImageMemoEntity>> by lazy {
//        getImageMemoUseCase.execute(book_idx)
//    }

    fun getIdxImageMemo() {
        _imageMemoList = getImageMemoUseCase.execute(book_idx)
    }

    fun backButton() {
        _backButton.value = Unit
    }

    fun getIdxBookInfo() {
        Log.i(TAG, "선택한 책 정보 호출")
        _bookInfo = getIdxBookInfoUseCase.execute(UserInfo.email, book_idx, reading_state)
    }

    fun deleteBook() {
        _deleteBook.value = Unit
    }

    fun deleteIdxBookInfo() {
        if (!checkNetworkState()) return
        deleteIdxBookInfoUseCase.execute(UserInfo.email, book_idx)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showProgress() }
            .doAfterTerminate {
                hideProgress()
                _backButton.value = Unit
            }
            .subscribe({
                Log.i(TAG, "책 삭제 성공")
            }, {
                Log.i(TAG, "책 삭제 오류 " + it.message.toString())
            })
    }

    fun showBookInfo() {
        _showBookInfo.value = Unit
    }

    fun contentsSeeDetail() {
        _contentsSeeDetail.value = Unit
    }

    fun changeMenu() {
        _changeMenu.value = Unit
    }

    fun changeSelectionMenu(state: String) {
        when (state) {
            MainViewModel.BEFORE_READ -> {
                if (!checkNetworkState()) return
                _selectedMenu.value = "읽을 책"
                compositeDisposable.add(
                    updateBookReadingStateUseCase.execute(UserInfo.email, book_idx, MainViewModel.BEFORE_READ)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { showProgress() }
                        .doAfterTerminate {
                            hideProgress()
                        }
                        .subscribe({
                            Log.i(TAG, "책 수정 성공")
                        }, {
                            Log.i(TAG, it.message.toString())
                        })
                )
            }
            MainViewModel.READING -> {
                if (!checkNetworkState()) return
                _selectedMenu.value = "읽는 중"
                compositeDisposable.add(
                    updateBookReadingStateUseCase.execute(UserInfo.email, book_idx, MainViewModel.READING)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { showProgress() }
                        .doAfterTerminate {
                            hideProgress()
                        }
                        .subscribe({
                            Log.i(TAG, "책 수정 성공")
                        }, {
                            Log.i(TAG, it.message.toString())
                        })
                )
            }
            MainViewModel.END_READ -> {
                if (!checkNetworkState()) return
                _selectedMenu.value = "읽은 책"
                compositeDisposable.add(
                    updateBookReadingStateUseCase.execute(UserInfo.email, book_idx, MainViewModel.END_READ)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { showProgress() }
                        .doAfterTerminate {
                            hideProgress()
                        }
                        .subscribe({
                            Log.i(TAG, "책 수정 성공")
                        }, {
                            Log.i(TAG, "책 수정 에러: " + it.message.toString())
                        })
                )
            }
        }
    }

    fun goToMap() {
        _goToMap.value = Unit
    }

    fun clickFloating() {
        _clickFloaing.value = Unit
    }

    fun insertImageMemo(file:File, fileName: String) {
        if (!checkNetworkState()) return
        compositeDisposable.add(
            insertImageMemo.execute(book_idx, file, fileName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .doAfterTerminate {
                    hideProgress()
                }
                .subscribe({
                    Log.i(TAG, "이미지 메모 저장 성공")
                }, {
                    Log.i(TAG, "이미지 메모 저장 에러: " + it.message.toString())
                })
        )
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
        const val TEXT = "TEXT"
        const val IMAGE = "IMAGE"
    }
}