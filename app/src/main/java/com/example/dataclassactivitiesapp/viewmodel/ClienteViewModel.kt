package com.example.dataclassactivitiesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dataclassactivitiesapp.data.model.Cliente
import com.example.dataclassactivitiesapp.data.repository.ClienteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClienteViewModel : ViewModel() {

    private val repository = ClienteRepository()

    private val _listState = MutableStateFlow<UiState<List<Cliente>>>(UiState.Idle)
    val listState: StateFlow<UiState<List<Cliente>>> = _listState

    private val _detailState = MutableStateFlow<UiState<Cliente>>(UiState.Idle)
    val detailState: StateFlow<UiState<Cliente>> = _detailState

    private val _opState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val opState: StateFlow<UiState<String>> = _opState

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
                _listState.value = UiState.Error(e.message ?: "Error al cargar clientes")
            }
        }
    }

    fun loadById(id: String) {
        viewModelScope.launch {
            _detailState.value = UiState.Loading
            try {
                val cliente = repository.getById(id)
                _detailState.value = if (cliente != null) UiState.Success(cliente)
                else UiState.Error("Cliente no encontrado")
            } catch (e: Exception) {
                _detailState.value = UiState.Error(e.message ?: "Error al cargar cliente")
            }
        }
    }

    fun create(cliente: Cliente) {
        viewModelScope.launch {
            _opState.value = UiState.Loading
            try {
                val id = repository.create(cliente)
                _opState.value = UiState.Success(id)
                loadAll()
            } catch (e: Exception) {
                _opState.value = UiState.Error(e.message ?: "Error al crear cliente")
            }
        }
    }

    fun update(cliente: Cliente) {
        viewModelScope.launch {
            _opState.value = UiState.Loading
            try {
                repository.update(cliente)
                _opState.value = UiState.Success(cliente.id)
                loadAll()
            } catch (e: Exception) {
                _opState.value = UiState.Error(e.message ?: "Error al actualizar cliente")
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
                _opState.value = UiState.Error(e.message ?: "Error al eliminar cliente")
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