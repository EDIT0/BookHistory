package com.ejstudio.bookhistory.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ejstudio.bookhistory.data.model.BookListEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable

@Dao
interface BookListDao {
    @Insert
    fun insertBook(bookListEntity: BookListEntity) : Completable

    /*@Update
    fun update(bookEntity: BookEntity)*/

    @Update
    fun updateBook(bookListEntity: BookListEntity)

//    @Query("UPDATE BookListEntity SET bookName = :newBookName WHERE idx = :idx")
//    fun updateBook(newBookName: String, idx: Int)

    @Delete
    fun deleteBook(bookListEntity: BookListEntity)

    @Query("DELETE FROM BookListEntity")
    fun deleteAllBookList()

    @Query("SELECT * FROM BookListEntity WHERE email = :email")
    fun getAllBookList(email: String): LiveData<List<BookListEntity>>

    @Query("SELECT * FROM BookListEntity WHERE email = :email and reading_state = :reading_state")
    fun getBookList(email: String, reading_state: String): LiveData<List<BookListEntity>>

    @Query("SELECT COUNT(*) FROM BookListEntity WHERE EXISTS ( SELECT * FROM BookListEntity WHERE email = :email and isbn = :isbn)")
    fun isExistBook(email: String, isbn: String): Int
}