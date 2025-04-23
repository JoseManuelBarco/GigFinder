package com.example.myapplication.api_objects

import com.google.gson.annotations.SerializedName

data class EventResponse(
    val id: Int,

    @SerializedName("date_start")  // Mapea el campo con guión bajo
    val dateStart: String,

    @SerializedName("date_end")    // Mapea el campo con guión bajo
    val dateEnd: String,

    val description: String,
    val price: Int,

    @SerializedName("Genre")       // Mapea el objeto anidado
    val genre: GenreResponse,      // Nueva clase para el género

    @SerializedName("Local")       // Mapea el objeto anidado
    val local: LocalResponse       // Nueva clase para el local
                        ) {
    // Clase auxiliar para el género
    data class GenreResponse(
        val id: Int,
        val name: String
                            )

    // Clase auxiliar para el local
    data class LocalResponse(
        val id: Int,
        val capacity: Int,
        val x_coordination: Double,
        val y_coordination: Double
                            )
}