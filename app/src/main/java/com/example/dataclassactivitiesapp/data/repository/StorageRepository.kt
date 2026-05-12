package com.example.dataclassactivitiesapp.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class StorageRepository {


    suspend fun uploadProductImage(imageUri: Uri, context: Context): String {
        return try {

            val directory = File(context.filesDir, "productos_images")
            if (!directory.exists()) {
                directory.mkdirs()
            }


            val fileName = "prod_${UUID.randomUUID()}.jpg"
            val file = File(directory, fileName)


            context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            } ?: throw Exception("No se pudo leer la imagen seleccionada")

            Log.d("StorageRepository", "Imagen guardada localmente en: ${file.absolutePath}")
            

            file.absolutePath
        } catch (e: Exception) {
            Log.e("StorageRepository", "Error al guardar imagen local: ${e.message}", e)
            throw Exception("Error al procesar la imagen localmente: ${e.localizedMessage}")
        }
    }

    suspend fun deleteByUrl(path: String) {
        if (path.isBlank()) return
        try {

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
