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

    @Insert
    fun insertTotalBookList(bookList: List<BookListEntity>) : Completable

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

//    @Query("UPDATE BOOKLISTENTITY SET reading_state = :reading_state WHERE email = :email and idx = :idx")
//    fun updateBookReadingState(email: String, idx: Int, reading_state: String): Completable

    @Query("UPDATE BOOKLISTENTITY SET reading_state = :reading_state, reading_start_datetime = :reading_start_datetime, reading_end_datetime = :reading_end_datetime, add_datetime = :add_datetime WHERE email = :email and idx = :idx")
    fun updateBookReadingState_before(email: String, idx: Int, reading_state: String, reading_start_datetime: String, reading_end_datetime: String, add_datetime: String): Completable

    @Query("UPDATE BOOKLISTENTITY SET reading_state = :reading_state, reading_start_datetime = :reading_start_datetime, reading_end_datetime = \"\" WHERE email = :email and idx = :idx")
    fun updateBookReadingState_reading(email: String, idx: Int, reading_state: String, reading_start_datetime: String): Completable

    @Query("UPDATE BOOKLISTENTITY SET reading_state = :reading_state, reading_end_datetime = :reading_end_datetime WHERE email = :email and idx = :idx")
    fun updateBookReadingState_end(email: String, idx: Int, reading_state: String, reading_end_datetime: String): Completable

    @Query("DELETE FROM BookListEntity WHERE email = :email and idx = :idx")
    fun deleteIdxBookInfo(email: String, idx: Int): Completable

    @Query("SELECT * FROM BookListEntity WHERE email = :email order by idx desc")
    fun getAllBookList(email: String): LiveData<List<BookListEntity>>

//    @Query("SELECT * FROM BookListEntity WHERE email = :email and reading_state = :reading_state order by idx desc")
//    fun getBookList(email: String, reading_state: String): LiveData<List<BookListEntity>>

    @Query("SELECT * FROM BookListEntity WHERE email = :email and reading_state = :reading_state order by add_datetime desc")
    fun getBookList_before(email: String, reading_state: String): LiveData<List<BookListEntity>>

    @Query("SELECT * FROM BookListEntity WHERE email = :email and reading_state = :reading_state order by reading_start_datetime desc")
    fun getBookList_reading(email: String, reading_state: String): LiveData<List<BookListEntity>>

    @Query("SELECT * FROM BookListEntity WHERE email = :email and reading_state = :reading_state and reading_end_datetime LIKE '%' || :pickerYear || '%' order by reading_end_datetime desc")
    fun getBookList_end(email: String, reading_state: String, pickerYear: String): LiveData<List<BookListEntity>>

    @Query("SELECT * FROM BookListEntity WHERE idx = :idx and email = :email and reading_state = :reading_state")
    fun getIdxBookInfo(email: String, idx: Int, reading_state: String): LiveData<BookListEntity>

    @Query("SELECT COUNT(*) FROM BookListEntity WHERE EXISTS ( SELECT * FROM BookListEntity WHERE email = :email and isbn = :isbn)")
    fun isExistBook(email: String, isbn: String): Int

    @Query("DELETE FROM BookListEntity WHERE email = :email")
    fun initRoomForCurrentUser(email: String) : Completable




//    @Query("DELETE FROM BookListEntity WHERE email=:email")
//    abstract fun deleteUserById(email: String)
//
//    @Query("DELETE FROM TextMemoEntity WHERE email=:email")
//    abstract fun cascadeDeletionsTextFromUser(email: String)
//
//    @Query("DELETE FROM ImageMemoEntity WHERE email=:email")
//    abstract fun cascadeDeletionsImageFromUser(email: String)
//
//    @Transaction
//    @Query("")
//    fun deleteUserWithCascade(email: String) {
//        cascadeDeletionsImageFromUser(email)
//        cascadeDeletionsTextFromUser(email)
//        deleteUserById(email)
//    }
}