package com.example.dataclassactivitiesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dataclassactivitiesapp.data.model.Producto
import com.example.dataclassactivitiesapp.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import com.example.dataclassactivitiesapp.data.repository.StorageRepository
import android.net.Uri
import android.content.Context

class ProductoViewModel : ViewModel() {

    private val repository = ProductoRepository()
    private val storageRepository = StorageRepository()

    // Estado de la lista
    private val _listState = MutableStateFlow<UiState<List<Producto>>>(UiState.Idle)
    val listState: StateFlow<UiState<List<Producto>>> = _listState

    // Estado del detalle / formulario
    private val _detailState = MutableStateFlow<UiState<Producto>>(UiState.Idle)
    val detailState: StateFlow<UiState<Producto>> = _detailState

    // Estado de operaciones (crear/editar/eliminar)
    private val _opState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val opState: StateFlow<UiState<String>> = _opState

    // Texto de búsqueda actual
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    fun loadAll() {
        viewModelScope.launch {
            _listState.value = UiState.Loading
            try {
                val list = repository.getAll()
                _listState.value = if (list.isEmpty()) UiState.Empty
                else UiState.Success(list)
            } catch (e: Exception) {
                _listState.value = UiState.Error(e.message ?: "Error al cargar productos")
            }
        }
    }

    fun loadById(id: String) {
        viewModelScope.launch {
            _detailState.value = UiState.Loading
            try {
                val producto = repository.getById(id)
                _detailState.value = if (producto != null) UiState.Success(producto)
                else UiState.Error("Producto no encontrado")
            } catch (e: Exception) {
                _detailState.value = UiState.Error(e.message ?: "Error al cargar producto")
            }
        }
    }

    fun create(producto: Producto) {
        viewModelScope.launch {
            _opState.value = UiState.Loading
            try {
                val id = repository.create(producto)
                _opState.value = UiState.Success(id)
                loadAll() // refresca la lista
            } catch (e: Exception) {
                _opState.value = UiState.Error(e.message ?: "Error al crear producto")
            }
        }
    }

    fun update(producto: Producto) {
        viewModelScope.launch {
            _opState.value = UiState.Loading
            try {
                repository.update(producto)
                _opState.value = UiState.Success(producto.id)
                loadAll()
            } catch (e: Exception) {
                _opState.value = UiState.Error(e.message ?: "Error al actualizar producto")
            }
        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            _opState.value = UiState.Loading
            try {
                repository.delete(id)
                _opState.value = UiState.Success(id)
                loadAll()
            } catch (e: Exception) {
                _opState.value = UiState.Error(e.message ?: "Error al eliminar producto")
            }
        }
    }

    fun uploadImageAndCreate(producto: Producto, imageUri: Uri?, context: Context) {
        viewModelScope.launch {
            _opState.value = UiState.Loading
            try {
                val imageUrl = if (imageUri != null) {
                    storageRepository.uploadProductImage(imageUri, context)
                } else ""

                val productoConImagen = producto.copy(imageUrl = imageUrl)
                val id = repository.create(productoConImagen)
                _opState.value = UiState.Success(id)
                loadAll()
            } catch (e: Exception) {
                _opState.value = UiState.Error(e.message ?: "Error al crear producto con imagen")
            }
        }
    }

    fun uploadImageAndUpdate(producto: Producto, imageUri: Uri?, context: Context) {
        viewModelScope.launch {
            _opState.value = UiState.Loading
            try {
                val imageUrl = if (imageUri != null) {
                    storageRepository.deleteByUrl(producto.imageUrl)
                    storageRepository.uploadProductImage(imageUri, context)
                } else producto.imageUrl  // conservar la imagen actual si no se cambió

                val productoConImagen = producto.copy(imageUrl = imageUrl)
                repository.update(productoConImagen)
                _opState.value = UiState.Success(productoConImagen.id)
                loadAll()
            } catch (e: Exception) {
                _opState.value = UiState.Error(e.message ?: "Error al actualizar producto con imagen")
            }
        }
    }

    fun search(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            _listState.value = UiState.Loading
            try {
                val list = if (query.isBlank()) repository.getAll()
                else repository.search(query)
                _listState.value = if (list.isEmpty()) UiState.Empty
                else UiState.Success(list)
            } catch (e: Exception) {
                _listState.value = UiState.Error(e.message ?: "Error en búsqueda")
            }
        }
    }

    fun resetOpState() { _opState.value = UiState.Idle }
}