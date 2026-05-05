package com.example.dataclassactivitiesapp

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dataclassactivitiesapp.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recuperar el objeto Celular enviado desde MainActivity
        val celular = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_CELULAR, Celular::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_CELULAR)
        }

        // Mostrar los datos en pantalla
        if (celular != null) {
            binding.tvCodigo.text = celular.codigo
            binding.tvMarca.text  = celular.marca
            binding.tvAnio.text   = celular.anioDeFabricacion.toString()
            binding.tvColor.text  = celular.color
            binding.tvEstilo.text = celular.estilo
        } else {
            val noData = getString(R.string.no_data)
            binding.tvCodigo.text = noData
            binding.tvMarca.text  = noData
            binding.tvAnio.text   = noData
            binding.tvColor.text  = noData
            binding.tvEstilo.text = noData
        }

        // Botón para regresar a MainActivity
        binding.btnVolver.setOnClickListener {
            finish()
        }
    }

    companion object {
        // Aquí se define la constante que usa MainActivity
        const val EXTRA_CELULAR = "extra_celular"
    }
}