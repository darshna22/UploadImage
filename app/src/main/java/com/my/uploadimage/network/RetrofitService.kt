package com.my.uploadimage.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitService private constructor() {
    val myApi: PostService

    init {
        val retrofit = Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        myApi = retrofit.create(PostService::class.java)
    }

    companion object {
        @get:Synchronized
        var instance: RetrofitService? = null
            get() {
                if (field == null) {
                    field = RetrofitService()
                }
                return field
            }
            private set
        const val url = "https://sapna.dev.nojoto.com/api/beta/"
    }
}