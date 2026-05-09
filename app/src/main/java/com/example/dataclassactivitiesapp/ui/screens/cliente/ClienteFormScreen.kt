package com.example.dataclassactivitiesapp.ui.screens.cliente

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dataclassactivitiesapp.data.model.Cliente
import com.example.dataclassactivitiesapp.viewmodel.ClienteViewModel
import com.example.dataclassactivitiesapp.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteFormScreen(
    clienteId: String? = null,
    onSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    vm: ClienteViewModel = viewModel()
) {
    val isEditing   = clienteId != null
    val detailState by vm.detailState.collectAsStateWithLifecycle()
    val opState     by vm.opState.collectAsStateWithLifecycle()

    var nombre    by remember { mutableStateOf("") }
    var apellido  by remember { mutableStateOf("") }
    var email     by remember { mutableStateOf("") }
    var telefono  by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }

    var nombreError   by remember { mutableStateOf(false) }
    var apellidoError by remember { mutableStateOf(false) }
    var emailError    by remember { mutableStateOf(false) }

    LaunchedEffect(clienteId) {
        if (isEditing) vm.loadById(clienteId!!)
    }

    LaunchedEffect(detailState) {
        if (isEditing && detailState is UiState.Success) {
            val c = (detailState as UiState.Success<Cliente>).data
            nombre    = c.nombre
            apellido  = c.apellido
            email     = c.email
            telefono  = c.telefono
            direccion = c.direccion
        }
    }

    LaunchedEffect(opState) {
        if (opState is UiState.Success) {
            vm.resetOpState()
            onSuccess()
        }
    }

    fun validate(): Boolean {
        nombreError   = nombre.isBlank()
        apellidoError = apellido.isBlank()
        emailError    = email.isBlank() || !email.contains("@")
        return !nombreError && !apellidoError && !emailError
    }

    fun submit() {
        if (!validate()) return
        val cliente = Cliente(
            id        = clienteId ?: "",
            nombre    = nombre.trim(),
            apellido  = apellido.trim(),
            email     = email.trim(),
            telefono  = telefono.trim(),
            direccion = direccion.trim()
        )
        if (isEditing) vm.update(cliente) else vm.create(cliente)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Editar Cliente" else "Nuevo Cliente") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (opState is UiState.Loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            if (opState is UiState.Error) {
                Text((opState as UiState.Error).message,
                    color = MaterialTheme.colorScheme.error)
            }

            OutlinedTextField(
                value = nombre, onValueChange = { nombre = it; nombreError = false },
                label = { Text("Nombre *") }, isError = nombreError,
                supportingText = { if (nombreError) Text("Campo requerido") },
                singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = apellido, onValueChange = { apellido = it; apellidoError = false },
                label = { Text("Apellido *") }, isError = apellidoError,
                supportingText = { if (apellidoError) Text("Campo requerido") },
                singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = email, onValueChange = { email = it; emailError = false },
                label = { Text("Email *") }, isError = emailError,
                supportingText = { if (emailError) Text("Email inválido") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = telefono, onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = direccion, onValueChange = { direccion = it },
                label = { Text("Dirección") },
                minLines = 2, maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { submit() },
                enabled = opState !is UiState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditing) "Guardar cambios" else "Crear cliente")
            }
        }
    }
}