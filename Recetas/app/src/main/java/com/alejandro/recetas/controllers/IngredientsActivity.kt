package com.alejandro.recetas.controllers


import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.recetas.databinding.ActivityIngredientsBinding
import com.alejandro.recetas.services.JsonService

class IngredientsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIngredientsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityIngredientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btExit.setOnClickListener {
            finish()
        }
        showIngredients()
    }

    /**
     * Show all ingredients. If some ingredient was repeated, it only shows in plural once.
     */
    private fun showIngredients() {
        val container = binding.lyIngredients
        container.removeAllViews()
        normalizeIngredients().forEach {
            container.addView(createCheckBox(it))
            addSeparatorView(container)
        }
    }

    private fun normalizeIngredients(): MutableList<String> {
        val recipes = JsonService.getRecipes(this) // Get all recipes

        val allIngredients = mutableListOf<String>()
        recipes?.forEach { recipe ->
            recipe.ingredients.forEach { allIngredients.add(it) }
        } // Add to a list all ingredients from all recipes

        val ingredientsWithUnits = mutableListOf<String>()
        allIngredients.forEach {
            if(it[0].isDigit()) ingredientsWithUnits.add(it)
        } // Filter ingredients that start with a digit (indicating quantity/unit)

        val restOfIngredients = mutableListOf<String>()
        allIngredients.forEach {
            if(!ingredientsWithUnits.contains(it)
                && !restOfIngredients.contains(it)) restOfIngredients.add(it)
        } //Add to another list the rest of ingredients that don't start with a digit

        val ingredientsWithoutUnits = mutableListOf<String>()
        ingredientsWithUnits.forEach {
            ingredientsWithoutUnits.add(it.substring(1).trim())
        } //Adds to another list the ingredients that has unit but with out it.

        val repeatedIngredientsWithUnits = mutableListOf<String>()
        ingredientsWithoutUnits.forEach { ingredients ->
            val words = ingredients.trim().lowercase().split(" ")
            val firstWord = words.first()
            val restOfWords = words.drop(1).joinToString(" ")

            val firstPlural = if (firstWord.endsWith("s")) firstWord else firstWord + "s"
            val firstSingle = if (firstWord.endsWith("s")) firstWord.dropLast(1) else firstWord

            val completePlural = "$firstPlural ${restOfWords}".trim()
            val completeSingular = "$firstSingle ${restOfWords}".trim()

            val occurrences = ingredientsWithoutUnits.count {
                it.equals(completePlural, ignoreCase = true) || it.equals(completeSingular, ignoreCase = true)
            }

            if (occurrences > 1 && !repeatedIngredientsWithUnits.contains(completePlural)) {
                repeatedIngredientsWithUnits.add(completePlural)
            }
        }//Adds to a list the ingredients that appears more than one time an have units

        //TODO: Ahora con toda esta lógica, hacer una lista que incluya los ingreddientes en plural, el resto de ingredientes,
        // y aquellos que teniendo unidades no estaban repetidos

        return repeatedIngredientsWithUnits
    }

    /**
     * Creates a CheckBox with specified text, typeface, and margin.
     */
    private fun createCheckBox(text: String, typeface: Int = Typeface.NORMAL, marginDp: Int = 10): CheckBox {
        return CheckBox(this).apply {
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