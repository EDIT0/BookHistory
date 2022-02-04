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

    @Query("UPDATE BOOKLISTENTITY SET reading_state = :reading_state WHERE email = :email and idx = :idx")
    fun updateBookReadingState(email: String, idx: Int, reading_state: String): Completable

    @Query("DELETE FROM BookListEntity WHERE email = :email and idx = :idx")
    fun deleteIdxBookInfo(email: String, idx: Int): Completable

    @Query("SELECT * FROM BookListEntity WHERE email = :email order by idx desc")
    fun getAllBookList(email: String): LiveData<List<BookListEntity>>

    @Query("SELECT * FROM BookListEntity WHERE email = :email and reading_state = :reading_state order by idx desc")
    fun getBookList(email: String, reading_state: String): LiveData<List<BookListEntity>>

    @Query("SELECT * FROM BookListEntity WHERE idx = :idx and email = :email and reading_state = :reading_state")
    fun getIdxBookInfo(email: String, idx: Int, reading_state: String): LiveData<BookListEntity>

    @Query("SELECT COUNT(*) FROM BookListEntity WHERE EXISTS ( SELECT * FROM BookListEntity WHERE email = :email and isbn = :isbn)")
    fun isExistBook(email: String, isbn: String): Int
}