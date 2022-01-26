package com.ejstudio.bookhistory.domain.model

data class SearchBookModel(
    var meta: Meta,
    var documents: List<Document>
) {
    data class Meta(
        var is_end: Boolean,
        var pageable_count: Int,
        var totalCount: Int
    ) {
    }

    data class Document(
        var authors: List<String>,
        var contents: String,
        var datetime: String,
        var isbn: String,
        var price: Int,
        var publisher: String,
        var sale_price: Int,
        var status: String,
        var thumbnail: String,
        var title: String,
        var translators: List<String>,
        var url: String
    ){

    }
}


