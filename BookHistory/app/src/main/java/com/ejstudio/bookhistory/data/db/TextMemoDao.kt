package com.ejstudio.bookhistory.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import io.reactivex.rxjava3.core.Completable

@Dao
interface TextMemoDao {
    @Insert
    fun insertTextMemo(textMemoEntity: TextMemoEntity)

    /*@Update
    fun update(bookEntity: BookEntity)*/

    @Update
    fun updateTextMemo(textMemoEntity: TextMemoEntity)

//    @Query("UPDATE BookListEntity SET bookName = :newBookName WHERE idx = :idx")
//    fun updateBook(newBookName: String, idx: Int)

    @Delete
    fun deleteTextMemo(textMemoEntity: TextMemoEntity)

    @Query("DELETE FROM TextMemoEntity")
    fun deleteAllTextMemo()

    @Query("SELECT * FROM TextMemoEntity WHERE booklist_idx = :booklist_idx")
    fun getAllTextMemo(booklist_idx: Int): LiveData<List<TextMemoEntity>>

    @Query("SELECT * FROM TextMemoEntity WHERE booklist_idx = :bookListIdx order by idx desc")
    fun getTotalTextMemo(bookListIdx: Int): LiveData<List<TextMemoEntity>>

    @Query("SELECT * FROM TextMemoEntity WHERE idx = :textMemoIdx")
    fun getIdxTextMemo(textMemoIdx: Int): LiveData<TextMemoEntity>

    @Query("DELETE FROM TextMemoEntity WHERE idx = :textMemoIdx")
    fun deleteIdxTextMemo(textMemoIdx: Int): Completable

    @Query("UPDATE TextMemoEntity SET memo_contents = :edit_memo_contents WHERE idx = :textMemoIdx")
    fun updateIdxTextMemo(textMemoIdx: Int, edit_memo_contents: String): Completable
}