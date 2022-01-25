package com.ejstudio.bookhistory.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.data.model.TextMemoEntity

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
}