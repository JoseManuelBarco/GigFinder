package com.example.myapplication.objects

class Musician(
    name: String,
    description: String,
    email: String,
    password: String,
    private val size: Int,
    private val price: Int,
    private val langId: Int,
    val genres: List<Int>  )