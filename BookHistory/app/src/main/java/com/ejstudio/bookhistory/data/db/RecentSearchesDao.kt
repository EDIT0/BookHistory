package com.ejstudio.bookhistory.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ejstudio.bookhistory.data.model.RecentSearchesEntity

@Dao
interface RecentSearchesDao {
    @Insert
    fun insertRecentSearchs(recentSearchesEntity: RecentSearchesEntity)

    /*@Update
    fun update(bookEntity: BookEntity)*/

    @Update
    fun updateRecentSearchs(recentSearchesEntity: RecentSearchesEntity)

//    @Query("UPDATE BookListEntity SET bookName = :newBookName WHERE idx = :idx")
//    fun updateBook(newBookName: String, idx: Int)

    @Delete
    fun deleteRecentSearchs(recentSearchesEntity: RecentSearchesEntity)

    @Query("DELETE FROM RecentSearchesEntity")
    fun deleteAllRecentSearchs()

    @Query("SELECT * FROM RecentSearchesEntity WHERE email = :email")
    fun getAllRecentSearchs(email: String): LiveData<List<RecentSearchesEntity>>
}