package com.example.myapplication.objects

class Musician(
    idUser: Int,
    name: String,
    description: String,
    email: String,
    password: String,
    rol: String,
    avg_rating: Int,
    image_identifier: String,
    private val size : Int,
    private val price: Int,
    private val song_lang: String):
    User(idUser, name, description, email, password, rol, avg_rating, image_identifier) {}