package com.example.kotlinnodejsauth.Retrofit

import com.example.kotlinnodejsauth.data.CommentItem
import com.example.kotlinnodejsauth.data.Video
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ServiceApi {

    @POST("Comment")
    @FormUrlEncoded
    fun CommentFile(
        @Field("id") id:Int?,
        @Field("Comment") Comment: String?,
        @Field("email") email:String?,
        @Field("name") name: String?): Observable<String>



    @get:GET("commentdata")
    val commentList: Observable<List<CommentItem>>
    @GET("commentdata/{id}")
    fun commentdata(@Path("id") id: Int?): Observable<List<CommentItem>>


    @POST("editComment")
    @FormUrlEncoded
    fun editcomment(
        @Field("idxx") idxx:Int?,
        @Field("id") id:Int?,
        @Field("Comment") Comment : String?,
        @Field("email") email: String?,
        @Field("name") name : String?,
        @Field("date")date: String?): Observable<String>

    @POST("deletecomment")
    @FormUrlEncoded
    fun deletecomment(
        @Field("idxx") idxx:Int?): Observable<String>

    @Multipart
    @POST("profile")
    fun uploadFile(
        @Part("title") title:String,
        @Part file: MultipartBody.Part,
        @Part file2: MultipartBody.Part,
        @Part("contents") contents:String): Call<String>

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

    @POST("register")
    @FormUrlEncoded
    fun registerUser(
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("password") password: String
    ): Observable<String>

    @POST("login")
    @FormUrlEncoded
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Observable<String>

    @POST("user")
    @FormUrlEncoded
    fun userInfoemail(
        @Field("email") email: String?):
            Observable<String>

    @POST("user")
    @FormUrlEncoded
    fun userInfoname(@Field("name") id: String?):
            Observable<String>








}

