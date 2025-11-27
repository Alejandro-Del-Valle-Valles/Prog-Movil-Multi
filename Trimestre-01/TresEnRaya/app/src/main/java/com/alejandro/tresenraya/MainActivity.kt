package com.alejandro.tresenraya

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.tresenraya.activities.GameActivity
import com.alejandro.tresenraya.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btPlay.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("PlayerOne", binding.etNamePlayerOne.text.toString())
            intent.putExtra("PlayerTwo", binding.etNamePlayerTwo.text.toString())
            startActivity(intent)
        }
    }
}