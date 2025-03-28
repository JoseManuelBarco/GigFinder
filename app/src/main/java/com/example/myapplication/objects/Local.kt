package com.example.myapplication.objects

class Local(
    idUser: Int? = null,  // Cambiado a nullable para que la base de datos lo asigne
                name: String,
                description: String,
                email: String,
                password: String,
                rol: String,
                avg_rating: Int,
                image_identifier: String,
                private val capacity: Int,
                private val x_coord: Double,
                private val y_coord: Double,):
    User(idUser, name, description, email, password, rol, avg_rating, image_identifier) {}