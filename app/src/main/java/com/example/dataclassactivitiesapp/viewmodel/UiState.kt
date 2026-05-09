package com.example.dataclassactivitiesapp.viewmodel

// Estado genérico reutilizable para cualquier lista de datos
sealed class UiState<out T> {
    object Idle    : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    object Empty   : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}