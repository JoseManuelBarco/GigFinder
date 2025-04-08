// ApiService.kt
package com.example.myapplication

import com.example.myapplication.api_objects.AuthInterceptor
import com.example.myapplication.api_objects.LoginRequest
import com.example.myapplication.objects.Event
import com.example.myapplication.objects.User
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface EventApiService {
    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<String>


    @GET("api/auth/whoami")
    suspend fun getUserProfile(
        @Header("Authorization") token: String // Cambié el encabezado a "Authorization"
                              ): Response<User>

    @POST("api/auth/signup/musician")
    suspend fun registerMusician(@Body musicianData: RequestBody): Response<Unit>

    @POST("api/auth/signup/local")
    suspend fun registerLocal(@Body localData: RequestBody): Response<Unit>

    @GET("api/Events")
    suspend fun getEvents(): List<Event>

}

object ApiClient {
    private const val BASE_URL = "http://10.0.0.99/dam03/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .build()

    val eventService: EventApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EventApiService::class.java)
    }
}