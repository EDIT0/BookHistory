package com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import com.ejstudio.bookhistory.domain.usecase.main.booklist.DeleteIdxTextMemoUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booklist.GetIdxTextMemoUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booklist.UpdateIdxTextMemoUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel
import com.ejstudio.bookhistory.util.UserInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class SeeTextMemoViewModel(
    private val getIdxTextMemoUseCase: GetIdxTextMemoUseCase,
    private val deleteIdxTextMemoUseCase: DeleteIdxTextMemoUseCase,
    private val updateIdxTextMemoUseCase: UpdateIdxTextMemoUseCase
) : BaseViewModel() {

    private val TAG = SeeTextMemoViewModel::class.java.simpleName
    private val _backButton: MutableLiveData<Unit> = MutableLiveData()
    val backButton: LiveData<Unit> get() = _backButton

    private val _editButton: MutableLiveData<Unit> = MutableLiveData()
    val editButton: LiveData<Unit> get() = _editButton

    private val _deleteTextMemo: MutableLiveData<Unit> = MutableLiveData()
    val deleteTextMemo: LiveData<Unit> get() = _deleteTextMemo

    var textMemoIdx = 0
    var bookTitle = ""
    var memo_contents = MutableLiveData<String>()
    var isEditMode = MutableLiveData<Boolean>()
    var edit_memo_contents= MutableLiveData<String>()
//    edit_memo_contents.value = memo_contents.value.toString()

    // 책 텍스트 메모 정보
//    lateinit var _textMemoList: LiveData<List<TextMemoEntity>>
    val textMemo: LiveData<TextMemoEntity> get() = _textMemo
    val _textMemo: LiveData<TextMemoEntity> by lazy {
        getIdxTextMemoUseCase.execute(textMemoIdx)
    }

    fun backButton() {
        _backButton.value = Unit
    }

    fun editButton() {
        _editButton.value = Unit
    }

    fun deleteTextMemo() {
        _deleteTextMemo.value = Unit
    }

    fun deleteIdxTextMemo() {
        deleteIdxTextMemoUseCase.execute(textMemoIdx)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showProgress() }
            .doAfterTerminate {
                hideProgress()
                _backButton.value = Unit
            }
            .subscribe({
                Log.i(TAG, "메모 삭제 성공")
            }, {
                Log.i(TAG, "메모 삭제 에러: " + it.message.toString())
            })
    }

    fun saveEditMemo() {
        updateIdxTextMemoUseCase.execute(textMemoIdx, edit_memo_contents.value.toString().trim())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showProgress() }
            .doAfterTerminate {
                hideProgress()
                textSynchronization()
                _backButton.value = Unit
            }
            .subscribe({
                Log.i(TAG, "메모 업데이트 성공")
            }, {
                Log.i(TAG, it.message.toString())
            })
    }

    fun textSynchronization() {
        edit_memo_contents.value = memo_contents.value.toString()
    }
}