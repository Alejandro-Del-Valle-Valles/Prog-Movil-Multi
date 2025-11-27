package com.alejandro.pruebasnotis

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.pruebasnotis.databinding.ActivityConfirmationBinding

class ConfirmationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    /** TODO:
     * El botón de cancelar muestra un Dialog con la opción "Si" si pulsa que si, vuelve al main
     * y muestra "Cancelado" en el TextView del main. Si pulsa en otro sitio se queda en la activity actual.
     * El botón de enviar muestra un Dialog con "Cancelar" y "Enviar" si cancela se queda en la actual activity
     * y si envia vuelve al main mostrando en el TextView "Enviado".
     */
}