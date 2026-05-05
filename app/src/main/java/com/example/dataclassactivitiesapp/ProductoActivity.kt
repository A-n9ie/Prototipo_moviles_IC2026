package com.example.dataclassactivitiesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dataclassactivitiesapp.databinding.ActivityProductoBinding

class ProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductoBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)

        if (!sessionManager.isLoggedIn()) {
            irALogin()
            return
        }

        binding = ActivityProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Producto"

        binding.btnGuardarProducto.setOnClickListener {
            guardarProducto()
        }
    }

    private fun guardarProducto() {
        val codigo    = binding.etCodigoProducto.text.toString().trim()
        val nombre    = binding.etNombreProducto.text.toString().trim()
        val categoria = binding.etCategoriaProducto.text.toString().trim()
        val precio    = binding.etPrecioProducto.text.toString().trim()
        val stock     = binding.etStockProducto.text.toString().trim()

        when {
            codigo.isEmpty() || nombre.isEmpty() || categoria.isEmpty() ||
                    precio.isEmpty() || stock.isEmpty() -> {
                Toast.makeText(this, "Complete todos los datos del producto", Toast.LENGTH_SHORT).show()
            }
            precio.toDoubleOrNull() == null -> {
                Toast.makeText(this, "El precio debe ser numérico", Toast.LENGTH_SHORT).show()
            }
            stock.toIntOrNull() == null -> {
                Toast.makeText(this, "El stock debe ser numérico", Toast.LENGTH_SHORT).show()
            }
            else -> {
                binding.tvResumenProducto.text =
                    "Código: $codigo\n" +
                            "Nombre: $nombre\n" +
                            "Categoría: $categoria\n" +
                            "Precio: ₡${"%.2f".format(precio.toDouble())}\n" +
                            "Stock: ${stock.toInt()}"
                Toast.makeText(this, "Datos del producto registrados", Toast.LENGTH_SHORT).show()
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