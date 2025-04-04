// User.kt
package com.example.myapplication.objects

data class User(
    val id: Int,
    val name: String,
    val description: String?,
    val type: String?,
    val avg_rating: Float?,
    val profile_image_identifier: String?//,
    //val genres: List<Genre>?,
   // val attachments: List<Any>,
   // val events: List<Any>
               )
