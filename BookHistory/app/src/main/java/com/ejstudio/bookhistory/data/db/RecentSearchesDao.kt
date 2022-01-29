package com.ejstudio.bookhistory.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ejstudio.bookhistory.data.model.RecentSearchesEntity
import io.reactivex.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
interface RecentSearchesDao {
    @Insert
    fun insertRecentSearches(recentSearchesEntity: RecentSearchesEntity) : io.reactivex.rxjava3.core.Completable

//    @Query("INSERT INTO RecentSearchesEntity VALUES(:email, :re)")
//    fun insertRecentSearches(email: String, re: String)

    /*@Update
    fun update(bookEntity: BookEntity)*/

    @Update
    fun updateRecentSearches(recentSearchesEntity: RecentSearchesEntity)

//    @Query("UPDATE BookListEntity SET bookName = :newBookName WHERE idx = :idx")
//    fun updateBook(newBookName: String, idx: Int)

//    @Delete
//    fun deleteRecentSearches(recentSearchesEntity: RecentSearchesEntity) : Completable

    @Query("DELETE FROM RecentSearchesEntity where idx = :deleteIdx")
    fun deleteRecentSearches(deleteIdx: Int) : io.reactivex.rxjava3.core.Completable

    @Query("DELETE FROM RecentSearchesEntity where email = :email")
    fun deleteAllRecentSearches(email: String) : io.reactivex.rxjava3.core.Completable

    @Query("SELECT * FROM RecentSearchesEntity WHERE email = :email order by idx desc")
    fun getAllRecentSearches(email: String): LiveData<List<RecentSearchesEntity>>
}