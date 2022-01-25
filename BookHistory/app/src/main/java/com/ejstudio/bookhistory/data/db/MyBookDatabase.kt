package com.ejstudio.bookhistory.data.db

import androidx.room.RoomDatabase
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.data.model.ImageMemoEntity
import com.ejstudio.bookhistory.data.model.TextMemoEntity

@androidx.room.Database(entities = [BookListEntity::class, TextMemoEntity::class, ImageMemoEntity::class], version = 1, exportSchema = false)
abstract class MyBookDatabase : RoomDatabase() {
    abstract fun bookListDao(): BookListDao
    abstract fun textMemoDao(): TextMemoDao
    abstract fun imageMemoDao(): ImageMemoDao
}