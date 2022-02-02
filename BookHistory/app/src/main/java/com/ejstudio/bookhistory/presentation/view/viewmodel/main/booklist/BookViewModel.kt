package com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.domain.usecase.main.booklist.DeleteIdxBookInfoUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booklist.GetIdxBookInfoUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel
import com.ejstudio.bookhistory.util.UserInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class BookViewModel(
    private val getIdxBookInfoUseCase: GetIdxBookInfoUseCase,
    private val deleteIdxBookInfoUseCase: DeleteIdxBookInfoUseCase
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

    var book_idx = 0
    var reading_state = ""

    var bookThumbnail = String()
    var bookTitle = MutableLiveData<String>()
    var bookAuthors = MutableLiveData<String>()
    var bookPublisher = String()
    var bookDatetime = String()
    var bookContents = String()
    var bookUrl = String()

    lateinit var _bookInfo: LiveData<BookListEntity>
    val bookInfo: LiveData<BookListEntity> get() = _bookInfo

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
}