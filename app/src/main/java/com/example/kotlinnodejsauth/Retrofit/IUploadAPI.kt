package com.example.kotlinnodejsauth.Retrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*


interface IUploadAPI {
    @Multipart
    @POST("profile")
    fun uploadFile(
        @Part("title") title:String,
        @Part file: MultipartBody.Part,
        @Part file2: MultipartBody.Part,
        @Part("contents") contents:String): Call<String>

}