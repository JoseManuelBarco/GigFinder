package com.example.myapplication.objects

data class Musician(
    val email: String,
    val name: String,
    val description: String,
    val password: String,
    val size: Int,
    val price: Int,
    val langId: Int,
    val genres: List<Int>
                   )