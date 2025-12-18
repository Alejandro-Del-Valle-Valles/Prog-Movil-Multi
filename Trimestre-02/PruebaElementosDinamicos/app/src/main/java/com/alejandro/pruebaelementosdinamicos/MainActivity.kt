package com.alejandro.pruebaelementosdinamicos

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.pruebaelementosdinamicos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.npChilds.minValue = 0
        binding.npChilds.maxValue = 4

        binding.npChilds.setOnValueChangedListener { picker, oldVal, newVal ->
            val diference = newVal - oldVal
            if(diference > 0) {
                for (i in 1..diference) {
                    val actualId = oldVal + i
                    addDinamicView(actualId)
                }
            } else if (diference < 0) {
                for (i in 1..Math.abs(diference)) {
                    deleteDinamicView()
                }
            }
        }
    }

    private fun addDinamicView(numeroHijo: Int) {
        val textView = TextView(this)
        textView.text = "Hijo $numeroHijo"

        val editTextName = EditText(this)
        editTextName.hint = "Nombre"
        editTextName.id = numeroHijo

        val editTextAge = EditText(this)
        editTextAge.hint = "Edad"
        editTextAge.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        editTextAge.id = numeroHijo + 100

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 16)

        textView.layoutParams = params
        editTextName.layoutParams = params
        editTextName.layoutParams = params

        binding.llChilds.addView(textView)
        binding.llChilds.addView(editTextName)
        binding.llChilds.addView(editTextAge)

        binding.svChildsData.post {
            binding.svChildsData.fullScroll(android.view.View.FOCUS_DOWN)
        }
    }

    private fun deleteDinamicView() {
        val count = binding.llChilds.childCount
        if (count >= 3) {
            binding.llChilds.removeViewAt(count - 1)
            binding.llChilds.removeViewAt(count - 2)
            binding.llChilds.removeViewAt(count - 3)
        }
    }
}