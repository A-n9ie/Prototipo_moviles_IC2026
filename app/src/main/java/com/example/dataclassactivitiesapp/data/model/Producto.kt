package com.example.dataclassactivitiesapp.data.model

data class Producto(
    val id: String = "",           // ID del documento en Firestore
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val stock: Int = 0,
    val imageUrl: String = ""      // URL de foto (Fase 5 — cámara)
)

// Convierte un Producto a Map para guardarlo en Firestore
fun Producto.toMap(): Map<String, Any> = mapOf(
    "nombre"      to nombre,
    "descripcion" to descripcion,
    "precio"      to precio,
    "stock"       to stock,
    "imageUrl"    to imageUrl
)

// Convierte un Map de Firestore a Producto
fun Map<String, Any>.toProducto(id: String) = Producto(
    id          = id,
    nombre      = this["nombre"]      as? String ?: "",
    descripcion = this["descripcion"] as? String ?: "",
    precio      = (this["precio"]     as? Number)?.toDouble() ?: 0.0,
    stock       = (this["stock"]      as? Number)?.toInt() ?: 0,
    imageUrl    = this["imageUrl"]    as? String ?: ""
)