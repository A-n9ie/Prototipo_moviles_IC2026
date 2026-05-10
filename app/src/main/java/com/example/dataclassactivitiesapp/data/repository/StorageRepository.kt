package com.example.dataclassactivitiesapp.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

/**
 * Repositorio para gestionar el almacenamiento de imágenes de forma LOCAL en el dispositivo.
 * Esto evita depender de Firebase Storage y soluciona los errores 404 de configuración.
 */
class StorageRepository {

    /**
     * Guarda una imagen en el almacenamiento interno privado de la aplicación.
     * @return La ruta absoluta del archivo guardado en el dispositivo.
     */
    suspend fun uploadProductImage(imageUri: Uri, context: Context): String {
        return try {
            // 1. Crear carpeta de destino en la memoria interna (privada de la app)
            val directory = File(context.filesDir, "productos_images")
            if (!directory.exists()) {
                directory.mkdirs()
            }

            // 2. Generar nombre de archivo único
            val fileName = "prod_${UUID.randomUUID()}.jpg"
            val file = File(directory, fileName)

            // 3. Copiar datos de la imagen seleccionada al archivo local
            context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            } ?: throw Exception("No se pudo leer la imagen seleccionada")

            Log.d("StorageRepository", "Imagen guardada localmente en: ${file.absolutePath}")
            
            // Retornamos la ruta absoluta que Coil puede cargar directamente
            file.absolutePath
        } catch (e: Exception) {
            Log.e("StorageRepository", "Error al guardar imagen local: ${e.message}", e)
            throw Exception("Error al procesar la imagen localmente: ${e.localizedMessage}")
        }
    }

    /**
     * Elimina un archivo del almacenamiento interno.
     */
    suspend fun deleteByUrl(path: String) {
        if (path.isBlank()) return
        try {
            // Si el path es una URL antigua de Firebase (http), no intentamos borrarla localmente
            if (path.startsWith("http")) return
            
            val file = File(path)
            if (file.exists()) {
                val deleted = file.delete()
                Log.d("StorageRepository", "Archivo local eliminado: $path ($deleted)")
            }
        } catch (e: Exception) {
            Log.w("StorageRepository", "No se pudo eliminar el archivo local: ${e.message}")
        }
    }
}
