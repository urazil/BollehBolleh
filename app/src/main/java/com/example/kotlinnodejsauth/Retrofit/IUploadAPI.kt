package com.example.kotlinnodejsauth.Retrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

//서버에 API를 정의하는 객채를 만든다.
interface IUploadAPI {
    @Multipart
    //영상볼래 게시물 등록
    @POST("profile")
    fun uploadFile(
        @Part("title") title:String,
        @Part file: MultipartBody.Part,
        @Part file2: MultipartBody.Part,
        @Part("contents") contents:String): Call<String>

}