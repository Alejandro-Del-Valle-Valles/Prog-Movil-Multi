package com.alejandro.calculadora

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.calculadora.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onClickedNumber(view: View) {

    }

    fun onClickedDecimal(view: View) {

    }

    fun onClickedPlus(view: View) {

    }

    fun onClickedMinus(view: View) {

    }

    fun onClickedMultiply(view: View) {

    }

    fun onClickedDivide(view: View) {

    }

    fun onClickedSquare(view: View) {

    }

    fun onClickedEqual(view: View) {

    }

    fun onClickedClear(view: View) {

    }

    fun onClickedDelete(view: View) {

    }

    fun onClickedChangeSymbol(view: View) {

    }
}