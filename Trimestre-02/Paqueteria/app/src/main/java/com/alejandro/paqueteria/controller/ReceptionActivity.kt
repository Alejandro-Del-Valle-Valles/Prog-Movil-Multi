package com.alejandro.paqueteria.controller

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.paqueteria.R
import com.alejandro.paqueteria.databinding.ActivityReceptionBinding
import com.alejandro.paqueteria.dialogs.WarningDialog
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
            if(intent.getBooleanExtra("notification", false)) {
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            finish()
        }
    }

    /**
     * Show the info of the package if the user comes from a notification, else show the number of certified letters.
     */
    private fun showInfo() {
        if(intent.getBooleanExtra("notification", false)) {
            binding.tvInformation.setText(buildPackageDataString())
            val isInsured = intent.getBooleanExtra("insured", false)
            if(isInsured) WarningDialog.newInstance(
                "Paquete asegurado", "El paquete está asegurado."
            ).show(supportFragmentManager, "WarningDialogTag")
        } else {
            val numberOfCertifiedLetters = MailRepository.getNumberOfCertificatedLetters()
            binding.tvInformation.setText("Se han recibido un total de $numberOfCertifiedLetters correos certificados")
        }
    }

    /**
     * Create a string with all data of the package.
     * @return String
     */
    private fun buildPackageDataString(): String {
        val sb = StringBuilder()
        val sender = intent.getStringExtra("sender")
        val destination = intent.getStringExtra("destination")
        val size = intent.getFloatArrayExtra("size")
        val weight = intent.getFloatExtra("weight", 0f)
        val isInsured = intent.getBooleanExtra("insured", false)

        sb.appendLine("Se ha recibido un paquete de $sender con destinatario $destination")
        sb.appendLine("Tiene un tamaño de ${size?.get(0)}cm de largo, por ${size?.get(1)}cm de alto, por ${size?.get(2)}cm de profundo.")
        sb.appendLine("El peso es de $weight Kg.")
        sb.appendLine("El paquete ${if(!isInsured) "NO" else ""} está asegurado.")
        return sb.toString()
    }
}