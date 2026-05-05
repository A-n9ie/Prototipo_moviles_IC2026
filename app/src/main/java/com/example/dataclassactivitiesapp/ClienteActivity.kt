package com.example.dataclassactivitiesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dataclassactivitiesapp.databinding.ActivityClienteBinding

class ClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClienteBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)

        if (!sessionManager.isLoggedIn()) {
            irALogin()
            return
        }

        binding = ActivityClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Cliente"

        binding.btnGuardarCliente.setOnClickListener {
            guardarCliente()
        }
    }

    private fun guardarCliente() {
        val nombre         = binding.etNombreCliente.text.toString().trim()
        val identificacion = binding.etIdentificacionCliente.text.toString().trim()
        val correo         = binding.etCorreoCliente.text.toString().trim()
        val telefono       = binding.etTelefonoCliente.text.toString().trim()
        val direccion      = binding.etDireccionCliente.text.toString().trim()

        when {
            nombre.isEmpty() || identificacion.isEmpty() || correo.isEmpty() ||
                    telefono.isEmpty() || direccion.isEmpty() -> {
                Toast.makeText(this, "Complete todos los datos del cliente", Toast.LENGTH_SHORT).show()
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> {
                Toast.makeText(this, "Ingrese un correo válido", Toast.LENGTH_SHORT).show()
            }
            else -> {
                binding.tvResumenCliente.text =
                    "Nombre: $nombre\n" +
                            "Identificación: $identificacion\n" +
                            "Correo: $correo\n" +
                            "Teléfono: $telefono\n" +
                            "Dirección: $direccion"
                Toast.makeText(this, "Datos del cliente registrados", Toast.LENGTH_SHORT).show()
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