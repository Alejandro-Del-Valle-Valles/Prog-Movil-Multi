package com.alejandro.paqueteria.controller

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.paqueteria.databinding.ActivityPackageSenderBinding
import com.alejandro.paqueteria.repository.MailRepository

class PackageSenderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPackageSenderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPackageSenderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btCancelPackage.setOnClickListener {
            finish()
        }

        binding.btSendPackage.setOnClickListener {
            if(CheckData()) {

            } else {
                //TODO: Mostrar un MaterialCardView notificando de que la info no está bien.
            }
        }
    }

    private fun CheckData(): Boolean {
        val isSenderCorrect = true
        val isDestinationCorrect = true
        val areDimensionsCorrect = true
        val isWeightCorrect = true
        //TODO: Comprobar los datos y notificar que falta o que es incorrecto
        return  isSenderCorrect && isDestinationCorrect && areDimensionsCorrect && isWeightCorrect
    }
}