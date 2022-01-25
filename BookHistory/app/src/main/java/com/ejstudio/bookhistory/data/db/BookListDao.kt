package com.ejstudio.bookhistory.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ejstudio.bookhistory.data.model.BookListEntity

@Dao
interface BookListDao {
    @Insert
    fun insertBook(bookListEntity: BookListEntity)

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
}