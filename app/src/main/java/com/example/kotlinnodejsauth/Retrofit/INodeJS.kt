package com.example.kotlinnodejsauth.Retrofit

import android.provider.ContactsContract
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface INodeJS {
    //회원가입
    @POST("register")
    @FormUrlEncoded
    fun registerUser(
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("password") password: String
    ): Observable<String>

     //로그인
    @POST("login")
    @FormUrlEncoded
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Observable<String>

     //유저 정보
    @POST("user")
    @FormUrlEncoded
    fun userInfo(@Field("email") email: String?): Observable<String>


    //내 정보 수정
    @POST("modify_myInfo")
    @FormUrlEncoded
    fun modify_myInfo(
        @Field("id") id : Int?,
        @Field("unique_id") unique_id: String?,
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("encrypted_password") encrypted_password: String?,
        @Field("salt") salt: String?,
        @Field("created_at") created_at: String?,
        @Field("updated_at") updated_at: String?
    ): Observable<String>

       //비밀번호 변경
    @POST("change_password")
    @FormUrlEncoded
    fun change_userPassword(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Observable<String>

    // 회원 탈퇴하기.
    @POST("delete_user")
    @FormUrlEncoded
    fun delete_user(
        @Field("id") id: Int?,
        @Field("email") email:String?
    ): Observable<String>



}