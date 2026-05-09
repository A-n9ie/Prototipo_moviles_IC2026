package com.example.dataclassactivitiesapp.data.repository

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

class StorageRepository {

    private val storage = FirebaseStorage.getInstance().reference

    // Sube una imagen y devuelve su URL de descarga pública
    suspend fun uploadProductImage(imageUri: Uri, context: android.content.Context): String {
        val filename = "productos/${UUID.randomUUID()}.jpg"
        val ref = storage.child(filename)

        // Leer bytes desde el Uri — funciona con cámara, galería y Downloads
        val inputStream = context.contentResolver.openInputStream(imageUri)
            ?: throw Exception("No se pudo leer la imagen seleccionada")
        val bytes = inputStream.use { it.readBytes() }

        ref.putBytes(bytes).await()
        return ref.downloadUrl.await().toString()
    }

    // Elimina una imagen dado su URL (opcional, para limpiar al borrar producto)
    suspend fun deleteByUrl(url: String) {
        if (url.isBlank()) return
        try {
            FirebaseStorage.getInstance().getReferenceFromUrl(url).delete().await()
        } catch (e: Exception) {
            // Si falla la eliminación de imagen, no bloqueamos la operación principal
        }
    }
}