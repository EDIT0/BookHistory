package com.ejstudio.bookhistory.domain.model

data class RecentPopularBookModel(
    val response: Response
) {
    data class Response(
        val request: Request,
        val resultNum: Int,
        val docs: List<Doc>
    ) {

        data class Request(
            val startDt: String,
            val endDt: String,
            val pageNo: Int,
            val pageSize: Int
        ) {

        }

        data class Doc(
            val doc: Doc__1
        ){
            data class Doc__1(
                val no: Int,
                val ranking: String,
                val bookname: String,
                val authors: String,
                val publisher: String,
                val publicationYear: String,
                val isbn13: String,
                val additionSymbol: String,
                val vol: String,
                val classNo: String,
                val classNm: String,
                val loanCount: String,
                val bookImageURL: String
            ) {

            }
        }

    }
}