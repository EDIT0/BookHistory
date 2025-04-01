package com.ejstudio.bookhistory.util

import com.ejstudio.bookhistory.BuildConfig
import com.ejstudio.bookhistory.data.api.ApiClient
import com.ejstudio.bookhistory.data.api.ApiInterface
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class ImageSenderModule {

    companion object mo {
        private var instance: ImageSenderModule? = null

        @JvmStatic
        fun getInstance(): ImageSenderModule = instance
            ?: synchronized(this){
                instance
                    ?: ImageSenderModule().also {
                        instance = it
                    }
            }
    }

    fun BaseModule(): ApiInterface {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()
        val client: ApiInterface = retrofit.create(ApiInterface::class.java)

        return client
    }

    fun SendImageModule(file: File, fileName: String): Pair<ApiInterface, MultipartBody.Part>{
//        var requestBody : RequestBody = RequestBody.create(MediaType.parse("image/jpg"),file)
        var requestBody : RequestBody = RequestBody.create("image/jpg".toMediaTypeOrNull(),file)
        var body : MultipartBody.Part = MultipartBody.Part.createFormData("uploaded_file", fileName, requestBody)

        var retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()

        var server = retrofit.create(ApiInterface::class.java)

        return Pair(server,body)
    }
}