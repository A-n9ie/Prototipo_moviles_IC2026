package com.example.dataclassactivitiesapp.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

class ProductoTest {

    @Test
    fun `cuando se convierte producto a mapa los campos coinciden`() {
        val producto = Producto(
            id = "123",
            nombre = "Café",
            descripcion = "Grano molido",
            precio = 2500.0,
            stock = 10,
            imageUrl = "ruta/local/foto.jpg"
        )

        val map = producto.toMap()

        assertEquals("Café", map["nombre"])
        assertEquals("Grano molido", map["descripcion"])
        assertEquals(2500.0, map["precio"])
        assertEquals(10, map["stock"])
        assertEquals("ruta/local/foto.jpg", map["imageUrl"])
    }

    @Test
    fun `cuando se convierte mapa a producto los datos se recuperan correctamente`() {
        val map = mapOf(
            "nombre" to "Azúcar",
            "descripcion" to "Blanca",
            "precio" to 1200.0,
            "stock" to 50,
            "imageUrl" to "ruta/azucar.jpg"
        )

        val producto = map.toProducto("ID-999")

        assertEquals("ID-999", producto.id)
        assertEquals("Azúcar", producto.nombre)
        assertEquals("Blanca", producto.descripcion)
        assertEquals(1200.0, producto.precio, 0.0)
        assertEquals(50, producto.stock)
        assertEquals("ruta/azucar.jpg", producto.imageUrl)
    }
}
