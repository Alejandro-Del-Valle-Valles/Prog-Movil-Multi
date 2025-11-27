package com.alejandro.testadapatdor02

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.testadapatdor02.adapter.AdaptadorBase
import com.alejandro.testadapatdor02.adapter.Adapter
import com.alejandro.testadapatdor02.databinding.ActivityMainBinding
import com.alejandro.testadapatdor02.model.Datos

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val datosPropios = arrayOf(
        Datos("Elemento 1", "Descricpión 1"),
        Datos("Elemento 2", "Descricpión 2"),
        Datos("Elemento 3", "Descricpión 3"),
        Datos("Elemento 4", "Descricpión 4"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adaptadorPropio = Adapter(this, datosPropios)
        binding.lvVista.adapter = adaptadorPropio

        var cabecera = layoutInflater.inflate(R.layout.cabecera, null)
        binding.lvVista.addHeaderView(cabecera)

        val adaptadorBase = AdaptadorBase(this, datosPropios)
        binding.spinner.adapter = adaptadorBase

        TODO("Implementar el Options Menu")
    }
}