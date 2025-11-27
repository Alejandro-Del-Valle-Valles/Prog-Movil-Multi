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
        binding.tvTextTwo.visibility = View.INVISIBLE

        val cs = ConstraintSet()
        cs.clone(binding.main)

        cs.connect(
            binding.tvTextThree.id,
            ConstraintSet.TOP,
            binding.tvTextOne.id,
            ConstraintSet.BOTTOM,
            10
        )

        cs.clear(
            binding.tvTextThree.id,
            ConstraintSet.BOTTOM
        )
        cs.applyTo(binding.main)
    }
}
