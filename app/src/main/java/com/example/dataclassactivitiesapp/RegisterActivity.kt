package com.example.dataclassactivitiesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dataclassactivitiesapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userManager: UserManager
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userManager = UserManager(this)
        sessionManager = SessionManager(this)

        binding.btnRegistrarse.setOnClickListener {
            registrarUsuario()
        }

        binding.btnIrLogin.setOnClickListener {
            finish()
        }
    }

    private fun registrarUsuario() {
        val usuario        = binding.etUsuario.text.toString().trim()
        val clave          = binding.etContrasena.text.toString().trim()
        val confirmarClave = binding.etConfirmarContrasena.text.toString().trim()

        when {
            usuario.isEmpty() || clave.isEmpty() || confirmarClave.isEmpty() -> {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            }
            usuario.length < 4 -> {
                Toast.makeText(this, "El usuario debe tener al menos 4 caracteres", Toast.LENGTH_SHORT).show()
            }
            clave.length < 4 -> {
                Toast.makeText(this, "La contraseña debe tener al menos 4 caracteres", Toast.LENGTH_SHORT).show()
            }
            clave != confirmarClave -> {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            }
            userManager.userExists(usuario) -> {
                Toast.makeText(this, "Ese usuario ya existe", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val registrado = userManager.registerUser(usuario, clave)
                if (registrado) {
                    sessionManager.login(usuario)
                    Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "No fue posible registrar el usuario", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}