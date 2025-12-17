package com.alejandro.ejercicio_apis.service

import com.alejandro.ejercicio_apis.model.PostBlog
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Query
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("/posts/{ID}")
    suspend fun getPost(@Query("id") id: Int?): Response<PostBlog>

    @POST("/posts")
    suspend fun postPost(@Body post: PostBlog): Response<PostBlog>
}