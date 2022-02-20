package com.ejstudio.bookhistory.domain.model

import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.data.model.ImageMemoEntity
import com.ejstudio.bookhistory.data.model.TextMemoEntity


data class TotalBookImageMemoListModel(
    var totalBookImageMemoListModel: List<ImageMemoEntity>
) {

//    data class BookListEntity(
//        var idx: Int,
//        val email: String,
//        val title: String,
//        val contents: String,
//        val url: String,
//        val isbn: String,
//        val datetime: String,
//        val authors: String,
//        val publisher: String,
//        val translators: String,
//        val price: String,
//        val sale_price: String,
//        val thumbnail: String,
//        val status: String,
//        val reading_state: String,
//        val add_datetime: String,
//        val reading_start_datetime: String,
//        val reading_end_datetime: String
//
//    ) {
//
//    }

//    data class BookList(
//        var authors: List<String>,
//        var contents: String,
//        var datetime: String,
//        var isbn: String,
//        var price: Int,
//        var publisher: String,
//        var sale_price: Int,
//        var status: String,
//        var thumbnail: String,
//        var title: String,
//        var translators: List<String>,
//        var url: String
//    ) : Serializable {
//        override fun toString(): String {
//            // 작가 리스트 스트링
//            var authorsString = StringBuilder()
//            for(i in 0 until authors.size) {
//                if(authors.size - 1 == i) {
//                    authorsString.append(authors.get(i))
//                } else {
//                    authorsString.append(authors.get(i) + ", ")
//                }
//            }
//
//            return authorsString.toString()
//        }
//    }
}