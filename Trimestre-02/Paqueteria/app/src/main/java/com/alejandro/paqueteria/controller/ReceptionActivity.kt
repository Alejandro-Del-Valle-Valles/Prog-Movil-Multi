package com.alejandro.paqueteria.controller

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.paqueteria.R
import com.alejandro.paqueteria.databinding.ActivityReceptionBinding
import com.alejandro.paqueteria.repository.MailRepository

class ReceptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReceptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReceptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showInfo()

        binding.btBack.setOnClickListener {
            finish()
        }
    }

    /**
     * Show the info of the package if the user comes from a notification, else show the number of certified letters.
     */
    private fun showInfo() {
        //TODO: Cuando viene de notificacion, no entra por aqui, solo por el else
        if(intent.getBooleanExtra("notification", false)) {
            //TODO: Mostrar toda la info del paquete
            val sender = intent.getStringExtra("Sender")
            binding.tvInformation.setText("Se ha recibido un paquete de ${if(sender != "") sender else "Desconocido"}")
        } else {
            val numberOfCertifiedLetters = MailRepository.getNumberOfCertificatedLetters()
            binding.tvInformation.setText("Se han recibido un total de $numberOfCertifiedLetters correos certificados")
        }
    }
}