package com.ejstudio.bookhistory.data.api

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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
}