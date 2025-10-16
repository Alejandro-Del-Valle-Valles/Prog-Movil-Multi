package com.alejandro.recetas.controllers

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.recetas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    /**
     * When Ingredients is clicked, starts the Ingredients Activity.
     */
    fun onIngredientsClicked(view: View) {
        val intent = Intent(this, IngredientsActivity::class.java)
        startActivity(intent)
    }

    /**
     * When Resume is clicked, starts the Resume Activity
     */
    fun onResumeClicked(view: View) {
        val intent = Intent(this, ResumeActivity::class.java)
        startActivity(intent)
    }

}