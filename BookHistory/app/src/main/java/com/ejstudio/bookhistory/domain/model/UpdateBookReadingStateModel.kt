package com.ejstudio.bookhistory.domain.model

data class UpdateBookReadingStateModel(
    var returnvalue: String,
    var reading_start_datetime: String,
    var reading_end_datetime: String,
    var add_datetime: String
) {
}