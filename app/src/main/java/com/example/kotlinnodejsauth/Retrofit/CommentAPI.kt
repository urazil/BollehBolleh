package com.example.kotlinnodejsauth.Retrofit

import com.example.kotlinnodejsauth.data.CommentItem
import io.reactivex.Observable
import retrofit2.http.*

interface CommentAPI {
    @POST("Comment")
    @FormUrlEncoded
    fun CommentFile(
        @Field("id") id:Int?,
        @Field("Comment") Comment: String?,
        @Field("email") email:String?,
        @Field("name") name: String?): Observable<String>


    @POST("user")
    @FormUrlEncoded
    fun userInfo(@Field("name") id: String?): Observable<String>

    @get:GET("commentdata")
    val commentList: Observable<List<CommentItem>>
    @GET("commentdata/{id}")
    fun commentdata(@Path("id") id: Int?): Observable<List<CommentItem>>


}