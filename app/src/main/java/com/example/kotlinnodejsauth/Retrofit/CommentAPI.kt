package com.example.kotlinnodejsauth.Retrofit

import com.example.kotlinnodejsauth.data.CommentItem
import com.example.kotlinnodejsauth.data.GalleryCommentItem
import io.reactivex.Observable
import retrofit2.http.*

interface CommentAPI {
    //영상볼래 댓글 등록
    @POST("Comment")
    @FormUrlEncoded
    fun CommentFile(
        @Field("id") id:Int?,
        @Field("Comment") Comment: String?,
        @Field("email") email:String?,
        @Field("name") name: String?): Observable<String>

    //사진볼래 댓글 등록
    @POST("gallerycomment")
    @FormUrlEncoded
    fun GalleryCommentFile(
        @Field("id") id:Int?,
        @Field("Comment") Comment: String?,
        @Field("email") email:String?,
        @Field("name") name: String?): Observable<String>


    //댓글 사용자
    @POST("user")
    @FormUrlEncoded
    fun userInfo(@Field("name") id: String?): Observable<String>

    //사진볼래 댓글 가져오기
    @get:GET("gallerycommentdata")
    val GallryCommentList: Observable<List<GalleryCommentItem>>
    @GET("gallerycommentdata/{id}")
    fun gallerycommentdata(@Path("id") id: Int?): Observable<List<GalleryCommentItem>>


    //영상볼래 댓글 가져오기
    @get:GET("commentdata")
    val commentList: Observable<List<CommentItem>>
    @GET("commentdata/{id}")
    fun commentdata(@Path("id") id: Int?): Observable<List<CommentItem>>

    //영상볼래 댓글 수정
    @POST("editComment")
    @FormUrlEncoded
    fun editcomment(
        @Field("idxx") idxx:Int?,
        @Field("id") id:Int?,
        @Field("Comment") Comment : String?,
        @Field("email") email: String?,
        @Field("name") name : String?,
        @Field("date")date: String?): Observable<String>

    //사진볼래 댓글 수정
    @POST("galleryeditComment")
    @FormUrlEncoded
    fun galleryeditcomment(
        @Field("idxx") idxx:Int?,
        @Field("id") id:Int?,
        @Field("Comment") Comment : String?,
        @Field("email") email: String?,
        @Field("name") name : String?,
        @Field("date")date: String?): Observable<String>

    //영상볼래 댓글 삭제
    @POST("deletecomment")
    @FormUrlEncoded
    fun deletecomment(
        @Field("idxx") idxx:Int?): Observable<String>

    //사진볼래 댓글 삭제
    @POST("gallerydeletecomment")
    @FormUrlEncoded
    fun gallerydeletecomment(
        @Field("idxx") idxx:Int?): Observable<String>






}