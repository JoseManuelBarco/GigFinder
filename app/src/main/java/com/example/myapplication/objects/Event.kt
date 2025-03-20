package com.example.myapplication.objects

import java.util.Date

class Event(
    private val idEvent: Int,
    private val description: String,
     val date_start: Date,
    private val date_end: Date,
    private val open_offer: Boolean,
    private val canceled: Boolean,
    private val price: Int,
    private val cancel_msg: String,
    private val password: String,
    private val rol: String,
    private val avg_rating: Int,
    private val image_identifier: String) {}