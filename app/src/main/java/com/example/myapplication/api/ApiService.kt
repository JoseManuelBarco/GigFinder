// ApiService.kt
package com.example.myapplication

import com.example.myapplication.objects.Local
import com.example.myapplication.objects.Musician
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface EventApiService {
    @GET("api/Events")
    suspend fun getEvents(): List<Map<String, Any>>

    @POST("api/auth/signup/local")
    suspend fun registerLocal(@Body localData: Local): Response<Map<String, Any>>

    @POST("api/auth/signup/musician")
    suspend fun registerMusician(@Body musicianData: Musician): Response<Map<String, Any>>

    // Nuevo endpoint para login
    @POST("api/auth/login")
    suspend fun login(@Body loginData: Map<String, String>): Response<Map<String, Any>>
}

object ApiClient {
    private const val BASE_URL = "http://10.0.0.99/dam03/"

    val eventService: EventApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EventApiService::class.java)
    }
}