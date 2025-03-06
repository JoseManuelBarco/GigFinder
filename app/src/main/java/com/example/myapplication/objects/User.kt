package com.example.myapplication.objects

abstract class User (
    private val idUser: Int,
    private val name: String,
    private val description: String,
    private val email: String,
    private val password: String,
    private val rol: String,
    private val avg_rating: Int,
    private val image_identifier: String) {}