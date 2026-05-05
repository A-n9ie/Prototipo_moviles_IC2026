package com.example.dataclassactivitiesapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class que representa un Celular con sus propiedades.
 * Implementa Parcelable para poder pasar el objeto entre Activities
 * usando un Intent mediante el mecanismo de parceling automático de Kotlin.
 */
@Parcelize
data class Celular(
    val codigo: String,
    val marca: String,
    val anioDeFabricacion: Int,
    val color: String,
    val estilo: String
) : Parcelable