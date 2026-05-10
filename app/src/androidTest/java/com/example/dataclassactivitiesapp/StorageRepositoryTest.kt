package com.example.dataclassactivitiesapp

import android.content.Context
import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.dataclassactivitiesapp.data.repository.StorageRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileOutputStream

@RunWith(AndroidJUnit4::class)
class StorageRepositoryTest {

    private lateinit var context: Context
    private lateinit var repository: StorageRepository

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        repository = StorageRepository()
    }

    @Test
    fun testGuardadoYBorradoLocalDeImagen() {
        // 1. Crear un archivo de prueba "falso" que simule una imagen
        val testFile = File(context.cacheDir, "test_input.jpg")
        FileOutputStream(testFile).use { it.write("fake image data".toByteArray()) }
        val uri = Uri.fromFile(testFile)

        // 2. Ejecutar la función de guardado del repositorio
        // Usamos runBlocking para pruebas en corrutinas
        kotlinx.coroutines.runBlocking {
            val savedPath = repository.uploadProductImage(uri, context)

            // 3. Verificaciones funcionales
            val savedFile = File(savedPath)
            assertTrue("El archivo debería existir en el disco", savedFile.exists())
            assertTrue("La ruta debe estar en la carpeta interna", savedPath.contains("productos_images"))

            // 4. Probar el borrado
            repository.deleteByUrl(savedPath)
            assertFalse("El archivo debería haber sido eliminado", savedFile.exists())
        }
    }
}
