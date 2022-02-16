package com.ejstudio.bookhistory.domain.model

data class TextImageMemoModel(
    val title: String,
    val idx: Int,
    val booklist_idx: Int,
    val memo_contents: String,
    val save_datetime: String,
    val email: String
) {
}