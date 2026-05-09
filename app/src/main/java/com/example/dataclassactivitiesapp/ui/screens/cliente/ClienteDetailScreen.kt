package com.example.dataclassactivitiesapp.ui.screens.cliente

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dataclassactivitiesapp.ui.screens.producto.DetailRow
import com.example.dataclassactivitiesapp.viewmodel.ClienteViewModel
import com.example.dataclassactivitiesapp.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteDetailScreen(
    clienteId: String,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    vm: ClienteViewModel = viewModel()
) {
    val detailState      by vm.detailState.collectAsStateWithLifecycle()
    val opState          by vm.opState.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(clienteId) { vm.loadById(clienteId) }

    LaunchedEffect(opState) {
        if (opState is UiState.Success) {
            vm.resetOpState()
            onNavigateBack()
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar cliente") },
            text  = { Text("¿Estás seguro de que deseas eliminar este cliente?") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    vm.delete(clienteId)
                }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Cliente") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(onClick = { onNavigateToEdit(clienteId) }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Editar",
                            tint = MaterialTheme.colorScheme.onPrimary)
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.onPrimary)
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
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when (val state = detailState) {
                is UiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is UiState.Error   -> Text(state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center))
                is UiState.Success -> {
                    val c = state.data
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (opState is UiState.Loading) {
                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        }
                        if (opState is UiState.Error) {
                            Text((opState as UiState.Error).message,
                                color = MaterialTheme.colorScheme.error)
                        }
                        DetailRow("Nombre", c.nombre)
                        DetailRow("Apellido", c.apellido)
                        DetailRow("Email", c.email)
                        DetailRow("Teléfono", c.telefono)
                        DetailRow("Dirección", c.direccion)
                    }
                }
                else -> Unit
            }
        }
    }
}