package com.alejandro.recetas.controllers

import android.graphics.Typeface
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.util.TypedValue
import android.view.View
import com.alejandro.recetas.databinding.ActivityResumeBinding
import com.alejandro.recetas.services.JsonService

class ResumeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResumeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResumeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showRecipe()
        binding.btGoMain.setOnClickListener {
            finish()
        }
    }

    /**
     * Displays the list of recipes with their ingredients and preparation steps.
     */
    private fun showRecipe() {
        val recipes = JsonService.getRecipes(this)
        val container = binding.lyRecipes
         container.removeAllViews()
        recipes?.forEach { recipe ->
            container.addView(
                createView(
                    recipe.title,
                    Typeface.BOLD,
                    20
                )
            )
            container.addView(
                createView(
                    "Ingredientes:\n${recipe.ingredients.joinToString(separator = "\n")}",
                    Typeface.ITALIC
                )
            )
            container.addView(
                createView(
                    "Preparación:\n${recipe.description}"
                )
            )
            addSeparatorView(container)
        }
    }

    /**
     * Creates a TextView with specified text, typeface, and margin.
     */
    private fun createView(text: String, typeface: Int = Typeface.NORMAL, marginDp: Int = 10): TextView {
        return TextView(this).apply {
            this.text = text
            setTypeface(null, typeface)
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val marginPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                marginDp.toFloat(),
                resources.displayMetrics
            ).toInt()
            params.setMargins(marginPx, marginPx, marginPx, marginPx)
            layoutParams = params
        }
    }

    /**
     * Adds a separator view between recipes.
     */
    private fun addSeparatorView(container: LinearLayout) {
        val separator = View(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                2
            ).apply {
                setMargins(0, 16, 0, 16)
            }
            setBackgroundColor(android.graphics.Color.LTGRAY)
        }
        container.addView(separator)
    }
}