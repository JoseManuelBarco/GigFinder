package com.example.myapplication.api_objects

data class EventPostBody(
    val dateStart: String,
    val dateEnd: String,
    val price: Int,
    val description: String,
    val genreId: Int
                        )