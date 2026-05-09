package com.example.dataclassactivitiesapp.data.model

data class Cliente(
    val id: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val email: String = "",
    val telefono: String = "",
    val direccion: String = ""
)

fun Cliente.toMap(): Map<String, Any> = mapOf(
    "nombre"    to nombre,
    "apellido"  to apellido,
    "email"     to email,
    "telefono"  to telefono,
    "direccion" to direccion
)

fun Map<String, Any>.toCliente(id: String) = Cliente(
    id        = id,
    nombre    = this["nombre"]    as? String ?: "",
    apellido  = this["apellido"]  as? String ?: "",
    email     = this["email"]     as? String ?: "",
    telefono  = this["telefono"]  as? String ?: "",
    direccion = this["direccion"] as? String ?: ""
)