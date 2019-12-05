package com.example.kotlinnodejsauth.Retrofit

import android.provider.ContactsContract
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface INodeJS {
    @POST("register")
    @FormUrlEncoded
    fun registerUser(@Field("email") email:String,
                     @Field("name") name:String,
                     @Field("password") password:String) : Observable<String>

    @POST("login")
    @FormUrlEncoded
    fun loginUser(@Field("email") email:String,
                  @Field("password") password:String) : Observable<String>


    @POST("user")
    @FormUrlEncoded
    fun userInfo(@Field("email") email: String?): Observable<String>






}