package com.alejandro.paqueteria.controller

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.paqueteria.databinding.ActivityLetterSenderBinding

class LetterSenderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLetterSenderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLetterSenderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btCancelLetter.setOnClickListener {
            finish()
        }

        binding.btSendLetter.setOnClickListener {
            if(checkData()) {
                finish()
            } else {
                //TODO: Añadir un MaterialCardView para notificar que los campos no están rellenados
            }
        }
    }

    /**
     * Check if the fields contains the required data and if not, shows a dialog that awares the user
     */
    private fun checkData(): Boolean {
        val isSenderCorrect = !binding.etSenderLetter.text.isNullOrBlank()
        val isDestinationCorrect = !binding.etDestinationLetter.text.isNullOrBlank()
        if(!isSenderCorrect) TODO("Mostrar un dialgo notificando de que el sender está vacío")
        if(!isDestinationCorrect) TODO("MOstrar un dialog notificando que el destination está vacío")
        return isSenderCorrect && isDestinationCorrect
    }
}