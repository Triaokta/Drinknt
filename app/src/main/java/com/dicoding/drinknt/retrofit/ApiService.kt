package com.dicoding.drinknt.retrofit

import com.dicoding.drinknt.data.DataLogin
import com.dicoding.drinknt.data.DataRegister
import com.dicoding.drinknt.data.DetailResponse
import com.dicoding.drinknt.data.LoginResponse
import com.dicoding.drinknt.data.ResponseStory
import com.dicoding.drinknt.data.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("login")
    fun userLogin(@Body requestLogin: DataLogin): Call<LoginResponse>

    @POST("register")
    fun userRegis(@Body requestRegister: DataRegister): Call<DetailResponse>

    @GET("stories")
    fun getStories(@Header("Authorization") token: String): Call<ResponseStory>

    @Multipart
    @POST("stories")
    fun uploadImg(
        @Part file:MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): Call<DetailResponse>

    @GET("name")
    fun getUserInfo(@Header("Authorization") token: String): Call<UserResponse>

}