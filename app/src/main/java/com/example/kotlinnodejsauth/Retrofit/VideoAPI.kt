package com.example.kotlinnodejsauth.Retrofit

import com.example.kotlinnodejsauth.data.CommentItem
import com.example.kotlinnodejsauth.data.Video
import io.reactivex.Observable
import okhttp3.MultipartBody
import org.w3c.dom.Comment
import retrofit2.Call
import retrofit2.http.*

interface VideoAPI {

    //영상볼래 게시물 가져오기
    @get:GET("videodata")
    val videoList: Observable<List<Video>>


    //영상볼래 게시물 삭제
    @POST("deletevideo")
    @FormUrlEncoded
    fun deleteFile(
        @Field("id")
        id: Int?
    ): Observable<String>

    //영상볼래 게시물 수정
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

