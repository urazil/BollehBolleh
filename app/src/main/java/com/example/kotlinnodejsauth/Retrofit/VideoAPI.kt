package com.example.kotlinnodejsauth.Retrofit

import com.example.kotlinnodejsauth.data.CommentItem
import com.example.kotlinnodejsauth.data.Video
import io.reactivex.Observable
import okhttp3.MultipartBody
import org.w3c.dom.Comment
import retrofit2.Call
import retrofit2.http.*

interface VideoAPI {
    @get:GET("videodata")
    val videoList: Observable<List<Video>>

    @POST("deletevideo")
    @FormUrlEncoded
    fun deleteFile(
        @Field("id")
        id: Int?
    ): Observable<String>

    @Multipart
    @POST("editvideo")
    fun editFile(
        @Part("title") title: String,
        @Part file: MultipartBody.Part,
        @Part file2: MultipartBody.Part,
        @Part("contents") contents: String,
        @Part("id") id: Int
    ): Call<String>



}

