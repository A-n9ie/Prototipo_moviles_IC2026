package com.example.dataclassactivitiesapp.ui.screens.producto

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
import com.example.dataclassactivitiesapp.data.model.Producto
import com.example.dataclassactivitiesapp.viewmodel.ProductoViewModel
import com.example.dataclassactivitiesapp.viewmodel.UiState

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoFormScreen(
    productoId: String? = null,       // null = crear, con ID = editar
    onSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    vm: ProductoViewModel = viewModel()
) {
    val isEditing   = productoId != null
    val detailState by vm.detailState.collectAsStateWithLifecycle()
    val opState     by vm.opState.collectAsStateWithLifecycle()

    // Campos del formulario
    var nombre      by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio      by remember { mutableStateOf("") }
    var stock       by remember { mutableStateOf("") }

    // Errores de validación
    var nombreError by remember { mutableStateOf(false) }
    var precioError by remember { mutableStateOf(false) }
    var stockError  by remember { mutableStateOf(false) }
    var imageUri    by remember { mutableStateOf<Uri?>(null) }
    val context     = LocalContext.current

    // Crea un archivo temporal para la foto
    val photoFile   = remember {
        File(context.getExternalFilesDir("Pictures"), "temp_producto.jpg")
    }
    val photoUri    = remember {
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", photoFile)
    }

    // Lanzador de la cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) imageUri = photoUri
    }

    // Lanzador para elegir de galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> if (uri != null) imageUri = uri }

    // Si estamos editando, cargar datos existentes
    LaunchedEffect(productoId) {
        if (isEditing) vm.loadById(productoId!!)
    }

    // Poblar campos cuando llegan los datos del producto a editar
    LaunchedEffect(detailState) {
        if (isEditing && detailState is UiState.Success) {
            val p = (detailState as UiState.Success<Producto>).data
            nombre      = p.nombre
            descripcion = p.descripcion
            precio      = p.precio.toString()
            stock       = p.stock.toString()
        }
    }

    // Navegar de vuelta al éxito
    LaunchedEffect(opState) {
        if (opState is UiState.Success) {
            vm.resetOpState()
            onSuccess()
        }
    }

    fun validate(): Boolean {
        nombreError = nombre.isBlank()
        precioError = precio.toDoubleOrNull() == null
        stockError  = stock.toIntOrNull() == null
        return !nombreError && !precioError && !stockError
    }

    fun submit() {
        if (!validate()) return
        val producto = Producto(
            id          = productoId ?: "",
            nombre      = nombre.trim(),
            descripcion = descripcion.trim(),
            precio      = precio.toDouble(),
            stock       = stock.toInt(),
            imageUrl    = ""
        )
        if (isEditing) vm.uploadImageAndUpdate(producto, imageUri, context)
        else           vm.uploadImageAndCreate(producto, imageUri, context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Editar Producto" else "Nuevo Producto") },
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
                value = nombre,
                onValueChange = { nombre = it; nombreError = false },
                label = { Text("Nombre *") },
                isError = nombreError,
                supportingText = { if (nombreError) Text("Campo requerido") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it; precioError = false },
                label = { Text("Precio *") },
                isError = precioError,
                supportingText = { if (precioError) Text("Ingrese un número válido") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                prefix = { Text("₡") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it; stockError = false },
                label = { Text("Stock *") },
                isError = stockError,
                supportingText = { if (stockError) Text("Ingrese un número entero válido") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider()

            Text("Foto del producto", style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)

            // Preview de la imagen seleccionada o la existente
            val imageToShow = imageUri
                ?: if (isEditing && detailState is UiState.Success) {
                    val url = (detailState as UiState.Success<Producto>).data.imageUrl
                    if (url.isNotBlank()) Uri.parse(url) else null
                } else null

            if (imageToShow != null) {
                AsyncImage(
                    model = imageToShow,
                    contentDescription = "Foto del producto",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

            // Botones para tomar foto o elegir de galería
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = { cameraLauncher.launch(photoUri) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = null,
                        modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Cámara")
                }
                OutlinedButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.Image, contentDescription = null,
                        modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Galería")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { submit() },
                enabled = opState !is UiState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditing) "Guardar cambios" else "Crear producto")
            }
        }
    }
}