package com.my.uploadimage.network

import okhttp3.RequestBody
import com.my.uploadimage.model.ImageApiResponse
import retrofit2.http.POST
import retrofit2.http.Multipart
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PartMap

interface PostService {
    @POST("content.php?cid=7ec99b415af3e88205250e3514ce0fa7")
    fun uploadImage(
        @Body requestBody: RequestBody?
    ): Call<ImageApiResponse?>?

    @Multipart
    @POST("content.php?cid=7ec99b415af3e88205250e3514ce0fa7")
    fun updateProfilePhotoProcess(@PartMap map: Map<String?, RequestBody?>?): Call<ImageApiResponse?>?
}