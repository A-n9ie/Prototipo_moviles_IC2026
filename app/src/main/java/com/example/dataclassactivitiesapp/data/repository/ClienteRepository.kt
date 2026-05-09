package com.example.dataclassactivitiesapp.data.repository

import com.example.dataclassactivitiesapp.data.model.Cliente
import com.example.dataclassactivitiesapp.data.model.toMap
import com.example.dataclassactivitiesapp.data.model.toCliente
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ClienteRepository {

    private val col = FirebaseFirestore.getInstance().collection("clientes")

    suspend fun getAll(): List<Cliente> {
        val snapshot = col.get().await()
        return snapshot.documents.mapNotNull { doc ->
            doc.data?.toCliente(doc.id)
        }
    }

    suspend fun getById(id: String): Cliente? {
        val doc = col.document(id).get().await()
        return doc.data?.toCliente(doc.id)
    }

    suspend fun create(cliente: Cliente): String {
        val ref = col.add(cliente.toMap()).await()
        return ref.id
    }

    suspend fun update(cliente: Cliente) {
        col.document(cliente.id).set(cliente.toMap()).await()
    }

    suspend fun delete(id: String) {
        col.document(id).delete().await()
    }

    suspend fun search(query: String): List<Cliente> {
        return getAll().filter {
            it.nombre.contains(query, ignoreCase = true) ||
                    it.apellido.contains(query, ignoreCase = true) ||
                    it.email.contains(query, ignoreCase = true)
        }
    }
}