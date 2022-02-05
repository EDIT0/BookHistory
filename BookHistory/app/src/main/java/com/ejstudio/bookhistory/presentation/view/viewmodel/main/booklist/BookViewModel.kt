package com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import com.ejstudio.bookhistory.domain.usecase.main.booklist.DeleteIdxBookInfoUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booklist.GetIdxBookInfoUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booklist.GetTextMemoUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booklist.UpdateBookReadingStateUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.MainViewModel
import com.ejstudio.bookhistory.util.UserInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class BookViewModel(
    private val getIdxBookInfoUseCase: GetIdxBookInfoUseCase,
    private val deleteIdxBookInfoUseCase: DeleteIdxBookInfoUseCase,
    private val updateBookReadingStateUseCase: UpdateBookReadingStateUseCase,
    private val getTextMemoUseCase: GetTextMemoUseCase
) : BaseViewModel() {

    private val TAG = BookViewModel::class.java.simpleName
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

    lateinit var _bookInfo: LiveData<BookListEntity>
    val bookInfo: LiveData<BookListEntity> get() = _bookInfo

    // 책 텍스트 메모 리스트
//    lateinit var _textMemoList: LiveData<List<TextMemoEntity>>
    val textMemoList: LiveData<List<TextMemoEntity>> get() = _textMemoList

    val _textMemoList: LiveData<List<TextMemoEntity>> by lazy {
        getTextMemoUseCase.execute(book_idx)
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
                Log.i(TAG, it.message.toString())
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

    fun clickFloating() {
        _clickFloaing.value = Unit
    }

    companion object {
        const val TEXT = "TEXT"
        const val IMAGE = "IMAGE"
    }
}