package com.example.myapplication.objects

import java.util.Date

class Event(
    val id: Int,
    val description: String,
    val date_start: String,
    val date_end: String,
    val price: Int,
    val opened_offer: Boolean,
    val canceled: Boolean,
    val cancel_msg: String,
    val Genre: Genre,
    val Local: LocalEvents) {}