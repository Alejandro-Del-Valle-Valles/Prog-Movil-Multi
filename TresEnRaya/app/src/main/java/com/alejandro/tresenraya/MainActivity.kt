package com.alejandro.tresenraya

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.tresenraya.Activities.GameActivity
import com.alejandro.tresenraya.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun onClickPlay(view: View) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("PalyerOne", binding.etNamePlayerOne.toString())
        intent.putExtra("PlayerTwo", binding.etNamePlayerTwo.toString())
        startActivity(intent)
    }
}