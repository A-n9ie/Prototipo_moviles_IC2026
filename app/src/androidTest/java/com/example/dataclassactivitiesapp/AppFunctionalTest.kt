package com.example.dataclassactivitiesapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AppFunctionalTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun test_01_Login_Exitoso() {
        composeTestRule.onNodeWithText("Correo electrónico").performTextInput("test@gmail.com")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("123456")
        composeTestRule.onNodeWithText("Ingresar").performClick()
        
        // Verificar que estamos en el Home
        composeTestRule.onNodeWithText("Módulos").assertIsDisplayed()
    }

    @Test
    fun test_02_Productos_Agregar() {
        composeTestRule.onNodeWithText("Productos").performClick()
        composeTestRule.onNodeWithContentDescription("Agregar producto").performClick()
        composeTestRule.onNodeWithText("Nombre *").performTextInput("Producto Prueba")
        composeTestRule.onNodeWithText("Precio *").performTextInput("1500")
        composeTestRule.onNodeWithText("Stock *").performTextInput("10")
        composeTestRule.onNodeWithText("Crear producto").performClick()
        
        // Verificar en la lista
        composeTestRule.onNodeWithText("Producto Prueba").assertExists()
    }

    @Test
    fun test_03_Productos_Editar() {
        composeTestRule.onNodeWithText("Productos").performClick()
        composeTestRule.onNodeWithText("Producto Prueba").performClick()
        composeTestRule.onNodeWithContentDescription("Editar").performClick()
        composeTestRule.onNodeWithText("Nombre *").performTextReplacement("Producto Modificado")
        composeTestRule.onNodeWithText("Guardar cambios").performClick()
        
        // Verificar cambio
        composeTestRule.onNodeWithText("Producto Modificado").assertExists()
    }

    @Test
    fun test_04_Productos_Eliminar() {
        composeTestRule.onNodeWithText("Productos").performClick()
        composeTestRule.onNodeWithText("Producto Modificado").performClick()
        composeTestRule.onNodeWithContentDescription("Eliminar").performClick()
        composeTestRule.onNodeWithText("Eliminar").performClick()
        
        // Verificar que ya no existe
        composeTestRule.onNodeWithText("Producto Modificado").assertDoesNotExist()
    }

    @Test
    fun test_05_Clientes_Agregar() {
        composeTestRule.onNodeWithText("Clientes").performClick()
        composeTestRule.onNodeWithContentDescription("Agregar cliente").performClick()
        composeTestRule.onNodeWithText("Nombre *").performTextInput("Pedro")
        composeTestRule.onNodeWithText("Apellido *").performTextInput("Ramirez")
        composeTestRule.onNodeWithText("Email *").performTextInput("pedro@mail.com")
        composeTestRule.onNodeWithText("Crear cliente").performClick()
        
        composeTestRule.onNodeWithText("Pedro Ramirez").assertExists()
    }

    @Test
    fun test_06_Clientes_Eliminar() {
        composeTestRule.onNodeWithText("Clientes").performClick()
        composeTestRule.onNodeWithText("Pedro Ramirez").performClick()
        composeTestRule.onNodeWithContentDescription("Eliminar").performClick()
        composeTestRule.onNodeWithText("Eliminar").performClick()
        
        composeTestRule.onNodeWithText("Pedro Ramirez").assertDoesNotExist()
    }
}
