package com.ejstudio.bookhistory.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "TextMemoEntity",
    foreignKeys = [ForeignKey(entity = BookListEntity::class, parentColumns = ["idx"], childColumns = ["booklist_idx"], onDelete = ForeignKey.CASCADE)]
)
data class TextMemoEntity(
    @PrimaryKey(autoGenerate = false)
    var idx: Int,
    val booklist_idx: Int,
    val memo_contents: String,
    val save_datetime: String
) {

}