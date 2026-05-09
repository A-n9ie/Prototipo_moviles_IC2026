package com.example.dataclassactivitiesapp.ui.screens.producto

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
import com.example.dataclassactivitiesapp.viewmodel.ProductoViewModel
import com.example.dataclassactivitiesapp.viewmodel.UiState

import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoDetailScreen(
    productoId: String,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    vm: ProductoViewModel = viewModel()
) {
    val detailState by vm.detailState.collectAsStateWithLifecycle()
    val opState     by vm.opState.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(productoId) { vm.loadById(productoId) }

    // Cuando se elimina exitosamente, volver a la lista
    LaunchedEffect(opState) {
        if (opState is UiState.Success) {
            vm.resetOpState()
            onNavigateBack()
        }
    }

    // Diálogo de confirmación de eliminación
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar producto") },
            text  = { Text("¿Estás seguro de que deseas eliminar este producto? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        vm.delete(productoId)
                    }
                ) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Producto") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    // Botón editar
                    IconButton(onClick = { onNavigateToEdit(productoId) }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Editar",
                            tint = MaterialTheme.colorScheme.onPrimary)
                    }
                    // Botón eliminar
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = detailState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is UiState.Error -> {
                    Text(
                        state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is UiState.Success -> {
                    val p = state.data
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Indicador de operación en curso
                        if (opState is UiState.Loading) {
                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        }
                        if (opState is UiState.Error) {
                            Text((opState as UiState.Error).message,
                                color = MaterialTheme.colorScheme.error)
                        }

                        DetailRow("Nombre",      p.nombre)
                        DetailRow("Descripción", p.descripcion)
                        DetailRow("Precio",      "₡${p.precio}")
                        DetailRow("Stock",       "${p.stock} unidades")
                        if (p.imageUrl.isNotBlank()) {
                            AsyncImage(
                                model = p.imageUrl,
                                contentDescription = "Foto de ${p.nombre}",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                            )
                        }
                    }
                }
                else -> Unit
            }
        }
    }
}

// Componente reutilizable para mostrar un campo etiqueta+valor
@Composable
fun DetailRow(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyLarge)
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    }
}