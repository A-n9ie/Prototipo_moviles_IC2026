package com.example.dataclassactivitiesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dataclassactivitiesapp.databinding.ActivityMenusBinding

class MenusActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenusBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)

        if (!sessionManager.isLoggedIn()) {
            irALogin()
            return
        }

        binding = ActivityMenusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Menús"

        binding.btnGuardarMenu.setOnClickListener {
            guardarMenu()
        }
    }

    private fun guardarMenu() {
        val nombre       = binding.etNombreMenu.text.toString().trim()
        val descripcion  = binding.etDescripcionMenu.text.toString().trim()
        val precio       = binding.etPrecioMenu.text.toString().trim()
        val disponible   = binding.etDisponibilidadMenu.text.toString().trim()

        when {
            nombre.isEmpty() || descripcion.isEmpty() ||
                    precio.isEmpty() || disponible.isEmpty() -> {
                Toast.makeText(this, "Complete todos los datos del menú", Toast.LENGTH_SHORT).show()
            }
            precio.toDoubleOrNull() == null -> {
                Toast.makeText(this, "El precio debe ser numérico", Toast.LENGTH_SHORT).show()
            }
            else -> {
                binding.tvResumenMenu.text =
                    "Nombre del menú: $nombre\n" +
                            "Descripción: $descripcion\n" +
                            "Precio: ₡${"%.2f".format(precio.toDouble())}\n" +
                            "Disponibilidad: $disponible"
                Toast.makeText(this, "Datos del menú registrados", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun irALogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}