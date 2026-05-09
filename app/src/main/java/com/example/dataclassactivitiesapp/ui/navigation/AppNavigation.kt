package com.example.dataclassactivitiesapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dataclassactivitiesapp.ui.screens.*
import com.example.dataclassactivitiesapp.ui.screens.cliente.ClienteDetailScreen
import com.example.dataclassactivitiesapp.ui.screens.cliente.ClienteFormScreen
import com.example.dataclassactivitiesapp.ui.screens.cliente.ClienteListScreen
import com.example.dataclassactivitiesapp.ui.screens.producto.ProductoDetailScreen
import com.example.dataclassactivitiesapp.ui.screens.producto.ProductoFormScreen
import com.example.dataclassactivitiesapp.ui.screens.producto.ProductoListScreen
import com.google.firebase.auth.FirebaseAuth

object Routes {
    const val LOGIN              = "login"
    const val REGISTER           = "register"
    const val HOME               = "home"
    const val PRODUCTO_LIST      = "producto_list"
    const val PRODUCTO_CREATE    = "producto_form"
    const val PRODUCTO_EDIT      = "producto_form/{id}"
    const val PRODUCTO_DETAIL    = "producto_detail/{id}"
    const val CLIENTE_LIST       = "cliente_list"
    const val CLIENTE_CREATE     = "cliente_form"
    const val CLIENTE_EDIT       = "cliente_form_edit/{id}"
    const val CLIENTE_DETAIL     = "cliente_detail/{id}"
}

@Composable
fun AppNavigation() {
    val navController  = rememberNavController()
    val startDestination = if (FirebaseAuth.getInstance().currentUser != null)
        Routes.HOME else Routes.LOGIN

    NavHost(navController = navController, startDestination = startDestination) {

        // ── Auth ────────────────────────────────────────────────────
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        // ── Home ────────────────────────────────────────────────────
        composable(Routes.HOME) {
            HomeScreen(
                onNavigateTo = { route ->
                    if (route != Routes.HOME) navController.navigate(route)
                },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }

        // ── Productos ───────────────────────────────────────────────
        composable(Routes.PRODUCTO_LIST) {
            ProductoListScreen(
                onNavigateToDetail = { id ->
                    navController.navigate("producto_detail/$id")
                },
                onNavigateToCreate = { navController.navigate(Routes.PRODUCTO_CREATE) },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Routes.PRODUCTO_CREATE) {
            ProductoFormScreen(
                productoId = null,
                onSuccess = { navController.popBackStack() },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Routes.PRODUCTO_EDIT) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            ProductoFormScreen(
                productoId = id,
                onSuccess = {
                    // Volver al detalle después de editar
                    navController.popBackStack()
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Routes.PRODUCTO_DETAIL) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            ProductoDetailScreen(
                productoId = id,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { navController.navigate("producto_form/$it") }
            )
        }

        // ── Clientes ────────────────────────────────────────────────
        composable(Routes.CLIENTE_LIST) {
            ClienteListScreen(
                onNavigateToDetail = { id ->
                    navController.navigate("cliente_detail/$id")
                },
                onNavigateToCreate = { navController.navigate(Routes.CLIENTE_CREATE) },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Routes.CLIENTE_CREATE) {
            ClienteFormScreen(
                clienteId = null,
                onSuccess = { navController.popBackStack() },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Routes.CLIENTE_EDIT) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            ClienteFormScreen(
                clienteId = id,
                onSuccess = { navController.popBackStack() },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Routes.CLIENTE_DETAIL) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            ClienteDetailScreen(
                clienteId = id,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { navController.navigate("cliente_form_edit/$it") }
            )
        }
    }
}