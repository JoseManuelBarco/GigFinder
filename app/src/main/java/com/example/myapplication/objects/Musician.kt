package com.example.myapplication.objects

class Musician(
    email: String,
    name: String,
    description: String,
    password: String,
    private val size: Int,
    private val price: Int,
    private val langId: Int,
    val genres: List<Int>
              )