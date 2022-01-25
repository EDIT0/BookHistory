package com.ejstudio.bookhistory.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ejstudio.bookhistory.data.model.ImageMemoEntity
import com.ejstudio.bookhistory.data.model.TextMemoEntity

@Dao
interface ImageMemoDao {
    @Insert
    fun insertImageMemo(imageMemoEntity: ImageMemoEntity)

    /*@Update
    fun update(bookEntity: BookEntity)*/

    @Update
    fun updateImageMemo(imageMemoEntity: ImageMemoEntity)

//    @Query("UPDATE BookListEntity SET bookName = :newBookName WHERE idx = :idx")
//    fun updateBook(newBookName: String, idx: Int)

    @Delete
    fun deleteImageMemo(imageMemoEntity: ImageMemoEntity)

    @Query("DELETE FROM ImageMemoEntity")
    fun deleteAllImageMemo()

    @Query("SELECT * FROM ImageMemoEntity WHERE booklist_idx = :booklist_idx")
    fun getAllImageMemo(booklist_idx: Int): LiveData<List<TextMemoEntity>>
}