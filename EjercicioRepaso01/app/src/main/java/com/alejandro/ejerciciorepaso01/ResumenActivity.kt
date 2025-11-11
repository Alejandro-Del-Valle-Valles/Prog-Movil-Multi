package com.alejandro.ejerciciorepaso01

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.alejandro.ejerciciorepaso01.databinding.ActivityResumenBinding

class ResumenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResumenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResumenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDatosIntent()
        binding.btSalirResumen.setOnClickListener {
            val intentMain = Intent(this, MainActivity::class.java)
            intentMain.putExtra("Cantidad 1", intent.getIntExtra("Cantidad 1", -1))
            intentMain.putExtra("Cantidad 2", intent.getIntExtra("Cantidad 2", -1))
            intentMain.putExtra("Cantidad 3", intent.getIntExtra("Cantidad 3", -1))
            intentMain.putExtra("Cantidad 4", intent.getIntExtra("Cantidad 4", -1))
            intentMain.putExtra("Cantidad 5", intent.getIntExtra("Cantidad 5", -1))
            intentMain.putExtra("Concepto 1", intent.getStringExtra("Concepto 1"))
            intentMain.putExtra("Concepto 2", intent.getStringExtra("Concepto 2"))
            intentMain.putExtra("Concepto 3", intent.getStringExtra("Concepto 3"))
            intentMain.putExtra("Concepto 4", intent.getStringExtra("Concepto 4"))
            intentMain.putExtra("Concepto 5", intent.getStringExtra("Concepto 5"))
            startActivity(intentMain)
            finish()
        }
    }

    private fun setDatosIntent() {
        var total = intent.getIntExtra("Cantidad 1", 0) +
                intent.getIntExtra("Cantidad 2", 0) +
                intent.getIntExtra("Cantidad 3", 0) +
                intent.getIntExtra("Cantidad 4", 0)
        binding.tvResumenUno.text = "${intent.getStringExtra("Concepto 1")} " +
                "${intent.getIntExtra("Cantidad 1", 0)}"
        binding.tvResumenUno.text = "${intent.getStringExtra("Concepto 2")} " +
                "${intent.getIntExtra("Cantidad 2", 0)}"
        binding.tvResumenUno.text = "${intent.getStringExtra("Concepto 3")} " +
                "${intent.getIntExtra("Cantidad 3", 0)}"
        binding.tvResumenUno.text = "${intent.getStringExtra("Concepto 4")} " +
                "${intent.getIntExtra("Cantidad 4", 0)}"
        if(intent.getStringExtra("Cantidad 5").isNullOrBlank()) {
            binding.tvResumenCinco.isVisible = false
        } else {
            binding.tvResumenUno.text = "${intent.getStringExtra("Concepto 5")} " +
                    "${intent.getIntExtra("Cantidad 5", 0)}"
            total += intent.getIntExtra("Cantidad 5", 0)
        }

        binding.tvTotal.text = "El total es de $total"
    }
}