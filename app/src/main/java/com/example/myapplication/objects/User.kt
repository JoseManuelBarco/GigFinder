package com.example.myapplication.objects

 open class User(
     private val idUser: Int? = null,  // idUser es nullable para ser asignado por la base de datos
     private val name: String,
     private val description: String = "No description", // Valor predeterminado
     private val email: String,
     private val password: String,
     private val rol: String = "User", // Valor predeterminado
     private val avg_rating: Int = 0, // Valor predeterminado
     private val image_identifier: String = "default_image" // Valor predeterminado
                ) {

    fun getEmail() : String{
        return email
    }

    fun getPassword(): String {
        return password
    }

}



