package com.ejstudio.bookhistory.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RecentSearchesEntity")
class RecentSearchesEntity(
    val email: String,
    val searchs: String
) {
    @PrimaryKey(autoGenerate = true)
    var idx: Int = 0
}