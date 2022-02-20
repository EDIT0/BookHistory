package com.ejstudio.bookhistory.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "BookListEntity")
data class BookListEntity(
    @PrimaryKey(autoGenerate = false)
    var idx: Int,
    val email: String,
    val title: String?,
    val contents: String?,
    val url: String?,
    val isbn: String?,
    val datetime: String?,
    val authors: String?,
    val publisher: String?,
    val translators: String?,
    val price: String?,
    val sale_price: String?,
    val thumbnail: String?,
    val status: String?,
    val reading_state: String?,
    val add_datetime: String?,
    val reading_start_datetime: String?,
    val reading_end_datetime: String?

    /*
    * idx int
    * email
    * title
    * contents
    * url
    * isbn
    * datetime
    * authors
    * publisher
    * translators
    * price
    * sale_price
    * thumbnail
    * status
    * reading_state
    * add_datetime
    * reading_start_datetime
    * reading_end_datetime
    * */
) {

}