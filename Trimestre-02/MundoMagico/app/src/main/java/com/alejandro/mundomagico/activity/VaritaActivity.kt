package com.alejandro.mundomagico.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.mundomagico.R
import com.alejandro.mundomagico.databinding.ActivityVaritaBinding

class VaritaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVaritaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVaritaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.getIntExtra("idVarita", -1) > -1) breakVaritaActions()

        binding.btExitCreateBreakVarita.setOnClickListener {
            finish()
        }

        /*TODO: Comprobar la información introducida es correcta y rellenar la información de la varita
        cuando viene de ListVaritaActivity.
        Mostrar dialog cuando se va a crear/romper una varita confirmando que se quiere realzar dicha acción
        También cuando se va a salir de la Activity
        Mostrar CardView cuando hay algún error o cuando se crea/rompe una varita
         */
    }

    /**
     * When the activity is started to destroy a Varita, this change some texts and info
     * to show the correct and needed information
     */
    private fun breakVaritaActions() {
        binding.tvCreateBreakeVarita.setText("Romper Varita")
        binding.cbIsBorken.visibility = View.GONE
        binding.btCreateBreak.setText("Romper Varita")
        binding.btCreateBreak.backgroundTintList = ColorStateList.valueOf(Color.YELLOW)
        binding.etWood.isEnabled = false
        binding.etCore.isEnabled = false
        binding.etLength.isEnabled = false
        //TODO: Rellenar información de los campos
    }
}