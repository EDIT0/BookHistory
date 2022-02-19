package com.ejstudio.bookhistory.data.api

import com.ejstudio.bookhistory.data.model.ImageMemoEntity
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import com.ejstudio.bookhistory.domain.model.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.Call
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
        @Field("pw") password:String,
        @Field("protectDuplicateLoginToken") protectDuplicateLoginToken: String
    ) : Observable<Unit>

    @FormUrlEncoded
    @POST("login/CheckEmail.php")
    fun checkEmail(
        @Field("email") email: String
    ) : Single<CheckTrueOrFalseModel>

    @Headers("Authorization: KakaoAK ${ApiClient.KAKAO_API_KEY}")
    @GET("${ApiClient.KAKAO_BASE_URL}v3/search/book")
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


    @GET("${ApiClient.BOOK_BASE_URL}loanItemSrch")
    fun getRecentPopularBook(
        @Query("authKey") authKey : String,
        @Query("startDt") startDt : String,
        @Query("endDt") endDt: String,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("format") format: String
    ) : Single<RecentPopularBookModel>

    @GET("${ApiClient.BOOK_BASE_URL}recommandList")
    fun getRecommendBook(
        @Query("authKey") authKey : String,
        @Query("isbn13") isbn13 : String,
        @Query("format") format: String
    ) : Single<RecommendBookModel>

    @GET("${ApiClient.BOOK_BASE_URL}loanItemSrch")
    fun getAlwaysPopularBook(
        @Query("authKey") authKey : String,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("format") format: String
    ) : Single<RecentPopularBookModel>

    @FormUrlEncoded
    @POST("main/DeleteIdxBookInfo.php")
    fun deleteIdxBookInfo(
        @Field("email") email: String,
        @Field("idx") idx: Int
    ) : Single<CheckTrueOrFalseModel>

    @FormUrlEncoded
    @POST("main/UpdateBookReadingState.php")
    fun updateBookReadingState(
        @Field("email") email: String,
        @Field("idx") idx: Int,
        @Field("reading_state") reading_state: String
    ) : Single<UpdateBookReadingStateModel>

    @FormUrlEncoded
    @POST("main/InsertTextMemo.php")
    fun insertTextMemo(
        @Field("bookIdx") bookIdx: Int,
        @Field("memoContents") memoContents: String,
        @Field("email") email: String
    ) : Single<TextMemoEntity>

    @FormUrlEncoded
    @POST("main/DeleteIdxTextMemo.php")
    fun deleteIdxTextMemo(
        @Field("textMemoIdx") textMemoIdx: Int
    ) : Single<CheckTrueOrFalseModel>

    @FormUrlEncoded
    @POST("main/UpdateIdxTextMemo.php")
    fun updateIdxTextMemo(
        @Field("textMemoIdx") textMemoIdx: Int,
        @Field("edit_memo_contents") edit_memo_contents: String
    ) : Single<CheckTrueOrFalseModel>

    @FormUrlEncoded
    @POST("main/InsertImageMemo.php")
    fun insertImageMemo(
        @Field("bookIdx") bookIdx: Int,
        @Field("imageMemoPath") imageMemoPath: String,
        @Field("email") email: String
    ) : Single<ImageMemoEntity>

    @Multipart
    @POST("main/ImageSenderToServer.php")
    fun imageSenderToServer(
        @Part("userId") userId: String,
        @Part imageFile : MultipartBody.Part
    ): Call<String>

    @FormUrlEncoded
    @POST("main/DeleteIdxImageMemo.php")
    fun deleteIdxImageMemo(
        @Field("imageMemoIdx") imageMemoIdx: Int
    ) : Single<CheckTrueOrFalseModel>

    @FormUrlEncoded
    @POST("login/UpdateProtectDuplicateLoginToken.php")
    fun updateProtectDuplicateLoginToken(
        @Field("email") email: String,
        @Field("protectDuplicateLoginToken") protectDuplicateLoginToken: String
    ) : Single<CheckTrueOrFalseModel>
}