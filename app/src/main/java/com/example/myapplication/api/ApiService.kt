// ApiService.kt
package com.example.myapplication

import com.example.myapplication.objects.AuthResponse
import com.example.myapplication.objects.Local
import com.example.myapplication.objects.LoginRequest
import com.example.myapplication.objects.Musician
import com.example.myapplication.objects.User
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path



interface EventApiService {
    @GET("api/Events")
    suspend fun getEvents(): List<Map<String, Any>>

    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<String>


    @GET("api/auth/whoami")
    suspend fun getUserProfile(
        @Header("Authorization") token: String // Cambié el encabezado a "Authorization"
                              ): Response<User>

}

object ApiClient {
    private const val BASE_URL = "http://10.0.0.99/dam03/"
    //añadir linea de token

    val eventService: EventApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EventApiService::class.java)
    }
}