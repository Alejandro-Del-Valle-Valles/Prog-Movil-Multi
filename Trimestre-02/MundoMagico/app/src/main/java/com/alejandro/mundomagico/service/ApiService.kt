package com.alejandro.mundomagico.service

import com.alejandro.mundomagico.model.CreateVaritaDTO
import com.alejandro.mundomagico.model.VaritaDTO
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("varita/moviles")
    suspend fun getAllVaritas(): Response<List<VaritaDTO>>

    @POST("varita")
    suspend fun createVarita(@Body varita: CreateVaritaDTO): Response<CreateVaritaDTO>

    @PUT("varita/rota/{id}")
    suspend fun breakVarita(@Path("id") id: Int): Response<ResponseBody>
}