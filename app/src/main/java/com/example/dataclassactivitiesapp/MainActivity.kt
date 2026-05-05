package com.example.dataclassactivitiesapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.dataclassactivitiesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)

        if (!sessionManager.isLoggedIn()) {
            irALogin()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarToolbarYDrawer()
        configurarPantallaInicio()
        configurarEventos()
    }

    private fun configurarToolbarYDrawer() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Panel principal"

        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )

        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> true
                R.id.nav_menus -> {
                    startActivity(Intent(this, MenusActivity::class.java))
                    true
                }
                R.id.nav_cliente -> {
                    startActivity(Intent(this, ClienteActivity::class.java))
                    true
                }
                R.id.nav_producto -> {
                    startActivity(Intent(this, ProductoActivity::class.java))
                    true
                }
                R.id.nav_cerrar_sesion -> {
                    confirmarCerrarSesion()
                    true
                }
                else -> false
            }.also {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
        }

        binding.navigationView.setCheckedItem(R.id.nav_inicio)
    }

    private fun configurarPantallaInicio() {
        val usuarioActual = sessionManager.getLoggedUser()
        binding.tvBienvenida.text = getString(R.string.welcome_user, usuarioActual)
        binding.tvDescripcion.text = getString(R.string.drawer_home_description)
    }

    private fun configurarEventos() {
        binding.btnIrMenus.setOnClickListener {
            startActivity(Intent(this, MenusActivity::class.java))
        }

        binding.btnIrCliente.setOnClickListener {
            startActivity(Intent(this, ClienteActivity::class.java))
        }

        binding.btnIrProducto.setOnClickListener {
            startActivity(Intent(this, ProductoActivity::class.java))
        }

        binding.btnCerrarSesion.setOnClickListener {
            confirmarCerrarSesion()
        }
    }

    private fun confirmarCerrarSesion() {
        AlertDialog.Builder(this)
            .setTitle("Cerrar sesión")
            .setMessage("¿Desea cerrar la sesión actual y volver al login?")
            .setPositiveButton("Sí") { _, _ ->
                sessionManager.logout()
                irALogin()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun irALogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}