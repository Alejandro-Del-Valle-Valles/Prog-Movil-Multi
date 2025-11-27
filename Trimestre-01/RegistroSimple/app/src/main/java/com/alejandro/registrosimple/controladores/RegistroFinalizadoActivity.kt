package com.alejandro.registrosimple.controladores

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.registrosimple.R
import com.alejandro.registrosimple.databinding.ActivityRegistroBinding
import com.alejandro.registrosimple.databinding.ActivityRegistroFinalizadoBinding

class RegistroFinalizadoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroFinalizadoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistroFinalizadoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}