package com.alejandro.pruebaselementoscodigo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import com.alejandro.pruebaselementoscodigo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btChange.setOnClickListener {
            deleteTvTwo()
        }
    }

    private fun deleteTvTwo() {
        binding.tvTextTwo.visibility = View.GONE

        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.main)

        constraintSet.connect(
            binding.tvTextThree.id, // El ID del view a modificar (tvTextThree)
            ConstraintSet.TOP,      // Su ancla superior (TOP)
            binding.tvTextOne.id,   // El ID del view al que se enlazará (tvTextOne)
            ConstraintSet.BOTTOM,    // El ancla inferior del view de destino (BOTTOM)
            10
        )
        constraintSet.applyTo(binding.main)
    }
}
