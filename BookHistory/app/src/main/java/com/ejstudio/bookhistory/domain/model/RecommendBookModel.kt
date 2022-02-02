package com.ejstudio.bookhistory.domain.model

data class RecommendBookModel(
    val response: Response
) {
    data class Response(
        val request: Request,
        val resultNum: Int,
        val docs: List<Doc>
    ) {
        data class Request(
            val isbn13: String
        ) {

        }

        data class Doc(
            val book: Book
        ) {
            data class Book(
                val no: Int,
                val bookname: String,
                val authors: String,
                val publisher: String,
                val publication_year: String,
                val isbn13: String,
                val addition_symbol: String,
                val vol: String,
                val class_no: String,
                val class_nm: String,
                val bookImageURL: String
            ){

            }
        }

    }
}