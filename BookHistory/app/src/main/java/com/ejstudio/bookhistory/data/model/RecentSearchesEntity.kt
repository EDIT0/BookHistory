package com.ejstudio.bookhistory.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RecentSearchesEntity")
class RecentSearchesEntity(
    @PrimaryKey
    val email: String,
    val searchs: String
) {

}