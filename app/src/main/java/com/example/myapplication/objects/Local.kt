package com.example.myapplication.objects
import kotlinx.serialization.Serializable

@Serializable
class Local(
                email: String,
                name: String,
                description: String,
                password: String,
                private val capacity: Int,
                private val x_coord: Double,
                private val y_coord: Double )