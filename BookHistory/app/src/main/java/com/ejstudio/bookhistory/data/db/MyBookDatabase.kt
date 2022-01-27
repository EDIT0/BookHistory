package com.ejstudio.bookhistory.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.data.model.ImageMemoEntity
import com.ejstudio.bookhistory.data.model.RecentSearchesEntity
import com.ejstudio.bookhistory.data.model.TextMemoEntity

@Database(entities = [BookListEntity::class, TextMemoEntity::class, ImageMemoEntity::class, RecentSearchesEntity::class], version = 1, exportSchema = false)
abstract class MyBookDatabase : RoomDatabase() {
    abstract fun bookListDao(): BookListDao
    abstract fun textMemoDao(): TextMemoDao
    abstract fun imageMemoDao(): ImageMemoDao
    abstract fun RecentSearchesDao(): RecentSearchesDao
}