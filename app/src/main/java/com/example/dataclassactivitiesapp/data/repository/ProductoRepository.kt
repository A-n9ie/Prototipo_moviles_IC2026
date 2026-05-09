package com.example.dataclassactivitiesapp.data.repository

import com.example.dataclassactivitiesapp.data.model.Producto
import com.example.dataclassactivitiesapp.data.model.toMap
import com.example.dataclassactivitiesapp.data.model.toProducto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProductoRepository {

    // Referencia a la colección "productos" en Firestore
    private val col = FirebaseFirestore.getInstance().collection("productos")

    // GET — obtener todos los productos
    suspend fun getAll(): List<Producto> {
        val snapshot = col.get().await()
        return snapshot.documents.mapNotNull { doc ->
            doc.data?.toProducto(doc.id)
        }
    }

    // GET — obtener un producto por ID
    suspend fun getById(id: String): Producto? {
        val doc = col.document(id).get().await()
        return doc.data?.toProducto(doc.id)
    }

    // POST — crear nuevo producto, Firestore genera el ID
    suspend fun create(producto: Producto): String {
        val ref = col.add(producto.toMap()).await()
        return ref.id
    }

    // PUT — actualizar producto existente
    suspend fun update(producto: Producto) {
        col.document(producto.id).set(producto.toMap()).await()
    }

    // DELETE — eliminar producto
    suspend fun delete(id: String) {
        col.document(id).delete().await()
    }

    // SEARCH — buscar por nombre (filtrado local)
    suspend fun search(query: String): List<Producto> {
        return getAll().filter {
            it.nombre.contains(query, ignoreCase = true) ||
                    it.descripcion.contains(query, ignoreCase = true)
        }
    }
}