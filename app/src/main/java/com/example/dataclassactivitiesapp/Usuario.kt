package com.example.dataclassactivitiesapp

/**
 * Data class que representa un Usuario con sus credenciales.
 * Se almacena en memoria mientras la app esté abierta.
 */
data class Usuario(
    val nombreUsuario: String,
    val contrasena: String
)

/**
 * Este data class no necesita @Parcelize porque no vamos a pasarlo entre Activities mediante un Inten
 * Se va a guardar en un objeto compartido.
 */