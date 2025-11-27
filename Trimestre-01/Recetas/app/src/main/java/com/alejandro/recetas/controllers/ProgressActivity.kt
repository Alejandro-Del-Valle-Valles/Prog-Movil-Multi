package com.alejandro.recetas.controllers

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.recetas.databinding.ActivityProgressBinding
import com.alejandro.recetas.services.JsonService

class ProgressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProgressBinding
    private lateinit var selectedIngredientSet: Set<String>
    private val skippableWords = setOf(
        "g", "kg", "mg", "l", "ml", "oz", "lb", "lbs",
        "de", "of", "a"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btExitProgress.setOnClickListener {
            finish()
        }

        val selectedArray = intent.getStringArrayExtra("selected_ingredients")

        //Convert to a Set for fast lookups.
        selectedIngredientSet = selectedArray?.toSet() ?: emptySet()
        showRecipe()
    }

    /**
     * Displays the list of recipes with their ingredients and preparation steps.
     * NOW: Also calculates progress, shows the progress bar, and colors missing ingredients.
     */
    private fun showRecipe() {
        val recipes = JsonService.getRecipes(this)
        val container = binding.lyProgress
        container.removeAllViews()

        recipes?.forEach { recipe ->
            // Get the list of original ingredients (e.g., "2 huevos", "1 taza de espinacas")
            val originalIngredients = recipe.ingredients.toList()

            // Calculate the progress percentage for *this* recipe
            val progress = calculateProgress(originalIngredients)

            // 1. Add Title
            container.addView(
                createView(
                    recipe.title,
                    Typeface.BOLD,
                    20
                )
            )

            // 2. Add Progress Bar (This function is now fixed)
            container.addView(createProgressBar(progress))

            // 3. Add Percentage Text
            container.addView(
                createView(
                    "$progress% Completo",
                    Typeface.ITALIC,
                    5 // Smaller margin
                )
            )

            // 4. Add "Ingredients:" Subtitle
            container.addView(
                createView(
                    "Ingredientes:",
                    Typeface.BOLD,
                    10
                )
            )

            // 5. Add individual ingredients, colored by availability
            originalIngredients.forEach { ingredient ->
                val hasIngredient = doesRecipeIngredientMatch(ingredient.lowercase())

                val ingredientView = createView(
                    "• $ingredient", // Add bullet point
                    Typeface.NORMAL,
                    2 // Smaller margin
                )

                if (!hasIngredient) {
                    // Highlight missing ingredients in RED
                    ingredientView.setTextColor(Color.RED)
                }
                container.addView(ingredientView)
            }

            // 6. Add Description
            container.addView(
                createView(
                    "Preparación:\n${recipe.description}",
                    Typeface.NORMAL,
                    10
                )
            )

            // 7. Add Separator
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
     * Creates a HORIZONTAL progress bar.
     */
    private fun createProgressBar(progress: Int): ProgressBar {
        // Use the horizontal style
        return ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal).apply {
            max = 100
            this.progress = progress
            isIndeterminate = false // Ensure it's not an indeterminate spinner

            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                val margin = 10 // Add some horizontal margin
                val marginPx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    margin.toFloat(),
                    resources.displayMetrics
                ).toInt()
                setMargins(marginPx, 4, marginPx, 4)
            }
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

    /**
     * THE CORE LOGIC:
     * Checks if a single original recipe ingredient (e.g., "1 huevo")
     * matches any of the normalized ingredients the user selected (e.g., "huevos").
     */
    private fun doesRecipeIngredientMatch(recipeIngredient: String): Boolean {
        val singularBase = getSingularBaseIngredient(recipeIngredient, skippableWords)
        val pluralBase = pluralizeBase(singularBase)

        //Check if the user's selected set contains EITHER the singular OR the plural form.
        return selectedIngredientSet.contains(singularBase) ||
                selectedIngredientSet.contains(pluralBase)
    }

    /**
     * Calculates the percentage of required ingredients that are available.
     * @param required List of all *original* ingredients needed for the recipe.
     * @return The compatibility percentage (0 to 100).
     */
    private fun calculateProgress(required: List<String>): Int {
        if (required.isEmpty()) return 100

        // Count how many required ingredients match our selected set.
        val matches = required.count { requiredIngredient ->
            doesRecipeIngredientMatch(requiredIngredient.lowercase())
        }

        // Calculate percentage: (matches / total) * 100
        return ((matches.toDouble() / required.size) * 100).toInt()
    }

    /**
     * (Copied from IngredientsActivity)
     * Extracts the base form of an ingredient and singularizes its first word.
     */
    private fun getSingularBaseIngredient(ingredient: String, skippableWords: Set<String>): String {
        val trimmed = ingredient.lowercase().trim()
        if (trimmed.isEmpty()) return ""
        val isUnitBased = trimmed.firstOrNull()?.isDigit() == true
        val base: String
        if (isUnitBased) {
            val words = trimmed.split(" ")
            var i = 1
            while (i < words.size && words[i].lowercase() in skippableWords) { i++ }
            base = words.drop(i).joinToString(" ")
        } else {
            base = trimmed
        }
        val baseWords = base.split(" ")
        val firstWord = baseWords.firstOrNull() ?: return ""
        val singularFirstWord = if (firstWord.endsWith("s")) firstWord.dropLast(1) else firstWord
        return "$singularFirstWord ${baseWords.drop(1).joinToString(" ")}".trim()
    }

    /**
     * (Copied from IngredientsActivity)
     * Pluralizes the first word of a base ingredient string.
     */
    private fun pluralizeBase(singularBase: String): String {
        val words = singularBase.split(" ")
        val firstWord = words.firstOrNull() ?: return singularBase
        val pluralFirstWord = if (firstWord.endsWith("s")) firstWord else firstWord + "s"
        return "$pluralFirstWord ${words.drop(1).joinToString(" ")}".trim()
    }
}