package com.example.dataclassactivitiesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dataclassactivitiesapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userManager: UserManager
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)
        userManager = UserManager(this)

        if (sessionManager.isLoggedIn()) {
            abrirMainYFinalizar()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIngresar.setOnClickListener {
            validarLogin()
        }

        binding.btnIrRegistro.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun validarLogin() {
        val usuario = binding.etUsuario.text.toString().trim()
        val clave   = binding.etContrasena.text.toString().trim()

        when {
            usuario.isEmpty() || clave.isEmpty() -> {
                Toast.makeText(this, "Ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
            }
            userManager.validateUser(usuario, clave) -> {
                sessionManager.login(usuario)
                Toast.makeText(this, "Bienvenido $usuario", Toast.LENGTH_SHORT).show()
                abrirMainYFinalizar()
            }
            else -> {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun abrirMainYFinalizar() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}