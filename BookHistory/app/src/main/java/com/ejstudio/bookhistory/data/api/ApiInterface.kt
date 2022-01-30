package com.ejstudio.bookhistory.data.api

import com.ejstudio.bookhistory.domain.model.CheckTrueOrFalseModel
import com.ejstudio.bookhistory.domain.model.SaveBookInfoModel
import com.ejstudio.bookhistory.domain.model.SearchBookModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface ApiInterface {
//    // query : 검색어, start : 시작, display : 갖고올개수
//    @GET("v1/search/movie.json")
//    fun getSearchMovie(
//        @Query("query") query: String,
//        @Query("start") start: Int = 1,
//        @Query("display") display: Int = 15
//    ): Single<MovieResponse>

//    @GET("movie")
//    fun get_movie(
//        @Query("query") query: String?,
//        @Query("language") language:String?,
//        @Query("page") page: String?,
//        @Query("api_key") api_key: String?,
//        @Query("include_adult") isAdult: Boolean?)
//            : Observable<MovieResult>

    @FormUrlEncoded
    @POST("login/SignUpEmailSender.php")
    fun emailSender(
        @Field("email") email: String,
        @Field("num") num: String
    ): Observable<String>

    @FormUrlEncoded
    @POST("login/RegisterEmailAndPassword.php")
    fun registerEmailAndPassword(
        @Field("email") email: String,
        @Field("pw") password:String
    ) : Observable<Unit>

    @FormUrlEncoded
    @POST("login/CheckEmail.php")
    fun checkEmail(
        @Field("email") email: String
    ) : Single<CheckTrueOrFalseModel>

    @Headers("Authorization: KakaoAK ${ApiClient.KAKAO_API_KEY}")
    @GET("https://dapi.kakao.com/v3/search/book")
    fun getSearchBook(
//        @Header("Authorization:KakaoAK ${ApiClient.KAKAO_API_KEY}") apiKey: String = ApiClient.KAKAO_API_KEY,
        @Query("query") query : String,
        @Query("sort") sort : String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : Observable<SearchBookModel>

    @FormUrlEncoded
    @POST("main/CheckIsExistBook.php")
    fun checkIsExistBook(
        @Field("email") email: String,
        @Field("isbn") isbn: String
    ) : Single<CheckTrueOrFalseModel>

    @FormUrlEncoded
    @POST("main/InsertBookInfo.php")
    fun insertBookInfo(
        @Field("email") email: String,
        @Field("authors") authors: String,
        @Field("contents") contents: String,
        @Field("datetime") datetime: String,
        @Field("isbn") isbn: String,
        @Field("price") price: String,
        @Field("publisher") publisher: String,
        @Field("sale_price") sale_price: String,
        @Field("status") status: String,
        @Field("thumbnail") thumbnail: String,
        @Field("title") title: String,
        @Field("translators") translators: String,
        @Field("url") url: String
    ) : Single<SaveBookInfoModel>
}