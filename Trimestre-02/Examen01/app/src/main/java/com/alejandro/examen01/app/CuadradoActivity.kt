package com.alejandro.examen01.app

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.examen01.databinding.ActivityCuadradoBinding
import com.alejandro.examen01.helper.Cuadrado

class CuadradoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCuadradoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCuadradoBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}