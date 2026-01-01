package com.alejandro.mundomagico.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.mundomagico.R
import com.alejandro.mundomagico.databinding.ActivityListVaritaBinding

class ListVaritaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListVaritaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityListVaritaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btExitManagment.setOnClickListener {
            finish()
        }

        /*TODO: Consultar la API y mostrar todas las varitas, además añadir funcionalidad para que
           al pulsar sobre una vartita, lance VaritaActivity con la información rellenada y permita romperla
        */
    }
}