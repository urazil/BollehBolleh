package com.example.kotlinnodejsauth.Retrofit

import com.example.kotlinnodejsauth.data.CommentItem
import com.example.kotlinnodejsauth.data.Photo
import com.example.kotlinnodejsauth.data.Video
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ServiceApi{

    //사진볼래 게시물 등록
    @Multipart
    @POST("picturedata")
    fun uploadPicFile(
        @Part("pictitle") pictitle:String?,
        @Part files : ArrayList<MultipartBody.Part>,
        @Part ("piccontents") piccontents: String?): Call<String>


    //사진볼래 게시물 가져오기
    @get:GET("photodata")
    val photoList: Observable<List<Photo>>


    //사진볼래 게시물 삭제
    @POST("deletephoto")
    @FormUrlEncoded
    fun deletePhotoFile(
        @Field("id")
        id: Int?
    ): Observable<String>

    //사진볼래 게시물 삭제
    @Multipart
    @POST("editpicturedata")
    fun editPicFile(
        @Part("pictitle") pictitle:String?,
        @Part files : ArrayList<MultipartBody.Part>,
        @Part ("piccontents") piccontents : String?,
        @Part("id") id: Int
    ): Call<String>


}