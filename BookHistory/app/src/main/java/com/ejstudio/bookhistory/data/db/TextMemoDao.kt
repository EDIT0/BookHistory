package com.ejstudio.bookhistory.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import com.ejstudio.bookhistory.domain.model.TextImageMemoModel
import io.reactivex.rxjava3.core.Completable

@Dao
interface TextMemoDao {
    @Insert
    fun insertTextMemo(textMemoEntity: TextMemoEntity)

    @Insert
    fun insertTotalBookTextMemoList(textMemoList: List<TextMemoEntity>) : Completable

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

    @Query("SELECT BookListEntity.title, TextMemoEntity.idx, TextMemoEntity.booklist_idx, TextMemoEntity.memo_contents, TextMemoEntity.save_datetime, TextMemoEntity.email " +
            "from TextMemoEntity, BookListEntity " +
            "WHERE TextMemoEntity.email= :email and BookListEntity.idx = TextMemoEntity.booklist_idx and save_datetime LIKE '%' || :calendarYear || '%' " +
            "union " +
            "SELECT BookListEntity.title, ImageMemoEntity.idx, ImageMemoEntity.booklist_idx, ImageMemoEntity.memo_image, ImageMemoEntity.save_datetime, ImageMemoEntity.email " +
            "from ImageMemoEntity, BookListEntity " +
            "WHERE ImageMemoEntity.email = :email and BookListEntity.idx = ImageMemoEntity.booklist_idx and save_datetime LIKE '%' || :calendarYear || '%'")
    fun getEmailTotalTextImageMemo(email: String, calendarYear: String): LiveData<List<TextImageMemoModel>>

    @Query("SELECT * FROM (SELECT BookListEntity.title, TextMemoEntity.idx, TextMemoEntity.booklist_idx, TextMemoEntity.memo_contents, TextMemoEntity.save_datetime, TextMemoEntity.email " +
            "from TextMemoEntity, BookListEntity " +
            "WHERE TextMemoEntity.email= :email and BookListEntity.idx = TextMemoEntity.booklist_idx and TextMemoEntity.save_datetime LIKE '%' || :calendarDate || '%' " +
            "union " +
            "SELECT BookListEntity.title, ImageMemoEntity.idx, ImageMemoEntity.booklist_idx, ImageMemoEntity.memo_image, ImageMemoEntity.save_datetime, ImageMemoEntity.email " +
            "from ImageMemoEntity, BookListEntity " +
            "WHERE ImageMemoEntity.email = :email and BookListEntity.idx = ImageMemoEntity.booklist_idx and ImageMemoEntity.save_datetime LIKE '%' || :calendarDate || '%') a order by save_datetime asc")
    fun getCalendarDateMemoList(email: String, calendarDate: String) : LiveData<List<TextImageMemoModel>>


//    @Query("SELECT BookListEntity.title, TextMemoEntity.idx, TextMemoEntity.booklist_idx, TextMemoEntity.memo_contents, TextMemoEntity.save_datetime, TextMemoEntity.email " +
//            "from TextMemoEntity, BookListEntity " +
//            "WHERE TextMemoEntity.email= :email and BookListEntity.idx = TextMemoEntity.booklist_idx and TextMemoEntity.save_datetime LIKE '%' || :calendarDate || '%' " +
//            "union " +
//            "SELECT BookListEntity.title, ImageMemoEntity.idx, ImageMemoEntity.booklist_idx, ImageMemoEntity.memo_image, ImageMemoEntity.save_datetime, ImageMemoEntity.email " +
//            "from ImageMemoEntity, BookListEntity " +
//            "WHERE ImageMemoEntity.email = :email and BookListEntity.idx = ImageMemoEntity.booklist_idx and ImageMemoEntity.save_datetime LIKE '%' || :calendarDate || '%'")
//    fun getCalendarDateMemoList(email: String, calendarDate: String) : LiveData<List<TextImageMemoModel>>

//    select bookhistory_textmemo.idx, bookhistory_textmemo.booklist_idx, bookhistory_textmemo.memo_contents, bookhistory_textmemo.save_datetime
//    from bookhistory_textmemo
//    WHERE bookhistory_textmemo.email="akdmadl34@naver.com"
//    UNION
//    select bookhistory_imagememo.idx, bookhistory_imagememo.booklist_idx, bookhistory_imagememo.memo_image, bookhistory_imagememo.save_datetime
//    from bookhistory_imagememo
//    WHERE bookhistory_imagememo.email="akdmadl34@naver.com"
}