package com.example.kotlinnodejsauth.Retrofit

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {

    private const val BaseURL = "http://10.0.2.2:3000/"
    private var retrofit: Retrofit? = null

    val instance: Retrofit
        get() {
            if (retrofit == null)
                retrofit = Retrofit.Builder()
                    .baseUrl(BaseURL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit!!
        }


}