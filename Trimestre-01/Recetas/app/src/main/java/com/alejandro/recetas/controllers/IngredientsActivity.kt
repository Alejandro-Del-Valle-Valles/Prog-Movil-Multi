package com.alejandro.recetas.controllers

import android.content.Context
import android.content.Intent
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
    private val selectionMap: MutableMap<String, Boolean> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityIngredientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ingredients = normalizeIngredients(this)

        // Populate the map with all ingredients, initially set to false.
        ingredients.forEach { ingredientName ->
            selectionMap[ingredientName] = false
        }

        // Show the checkboxes
        showIngredients(ingredients)

        binding.btExit.setOnClickListener {
            finish()
        }

        binding.btProgress.setOnClickListener {
            // Filter the map to get only the ingredients that are checked (value == true)
            val selectedIngredients = selectionMap
                .filter { it.value }
                .keys
                .toTypedArray() // Convert the resulting List<String> to Array<String>

            val intent = Intent(this, ProgressActivity::class.java)

            // Send ONLY the selected ingredients
            intent.putExtra("selected_ingredients", selectedIngredients)
            startActivity(intent)
            finish()
        }
    }

    /**
     * Show all ingredients. If some ingredient was repeated, it only shows in plural once.
     */
    private fun showIngredients(ingredients: MutableList<String>) {
        val container = binding.lyIngredients
        container.removeAllViews()
        ingredients.forEach { ingredientName ->
            // Create the checkbox and add its listener
            val checkBox = createCheckBox(ingredientName)

            // Add a listener that updates our map when the state changes.
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                selectionMap[ingredientName] = isChecked
            }

            container.addView(checkBox)
            addSeparatorView(container)
        }
    }

    /**
     * Normalizes the ingredient list from all recipes.
     *
     * This function extracts all ingredients from all recipes and processes them
     * to create a normalized list.
     *
     * @param context The context, needed by JsonService.
     * @return A mutable list of unique, normalized ingredient strings.
     */
    private fun normalizeIngredients(context: Context): MutableList<String> {

        // and Recipe has a property 'ingredients' which is Array<String>
        val recipes = JsonService.getRecipes(context)

        // 1. Flatten all ingredient lists into one, trimming and lowercasing.
        val allIngredients = recipes?.flatMap { it.ingredients.toList() }
            ?.map { it.lowercase().trim() }
            ?: emptyList()


        val skippableWords = setOf(
            "g", "kg", "mg", "l", "ml", "oz", "lb", "lbs",
            "de", "of", "a"
        )

        // 3. Group all original ingredients by their singular base form.
        val baseIngredientMap = mutableMapOf<String, MutableList<String>>()

        allIngredients.forEach { ingredient ->
            val singularBase = getSingularBaseIngredient(ingredient, skippableWords)
            if (singularBase.isNotBlank()) {
                baseIngredientMap.getOrPut(singularBase) { mutableListOf() }
                    .add(ingredient)
            }
        }

        // 4. Process the map to create the final, normalized list.
        val finalIngredientSet = mutableSetOf<String>()

        baseIngredientMap.forEach { (singularKey, originalList) ->

            // Check if any of the original ingredients did NOT start with a number.
            val hasNonUnitIngredient = originalList.any { !it.firstOrNull()?.isDigit()!! }

            if (hasNonUnitIngredient) {
                finalIngredientSet.add(singularKey)
            } else {
                if (originalList.size > 1) {
                    finalIngredientSet.add(pluralizeBase(singularKey))
                } else {
                    finalIngredientSet.add(singularKey)
                }
            }
        }

        // Return the final unique set as a mutable list.
        return finalIngredientSet.toMutableList()
    }

    /**
     * Extracts the base form of an ingredient and singularizes its first word.
     * @param ingredient The raw ingredient string (e.g., "2 eggs").
     * @param skippableWords A set of unit/preposition words to ignore.
     * @return The singular base ingredient (e.g., "egg").
     */
    private fun getSingularBaseIngredient(ingredient: String, skippableWords: Set<String>): String {
        val trimmed = ingredient.lowercase().trim()
        if (trimmed.isEmpty()) return ""

        val isUnitBased = trimmed.firstOrNull()?.isDigit() == true
        val base: String

        if (isUnitBased) {
            // Ingredient starts with a number
            val words = trimmed.split(" ")

            // Find the index of the first word that is NOT a number or a skippable word.
            var i = 1 // Start at index 1 to skip the number
            while (i < words.size && words[i].lowercase() in skippableWords) {
                i++
            }

            // The rest of the words form the base ingredient
            base = words.drop(i).joinToString(" ")
        } else {
            // Ingredient does not start with a number
            base = trimmed
        }

        // Now, singularize the *first word* of the base ingredient
        val baseWords = base.split(" ")
        val firstWord = baseWords.firstOrNull() ?: return ""

        val singularFirstWord = if (firstWord.endsWith("s")) {
            firstWord.dropLast(1)
        } else {
            firstWord
        }

        return "$singularFirstWord ${baseWords.drop(1).joinToString(" ")}".trim()
    }

    /**
     * Pluralizes the first word of a base ingredient string.
     * @param singularBase The singular base ingredient (e.g., "egg").
     * @return The pluralized base ingredient (e.g., "eggs").
     */
    private fun pluralizeBase(singularBase: String): String {
        val words = singularBase.split(" ")
        val firstWord = words.firstOrNull() ?: return singularBase

        // Simple pluralization rule: add 's' if it's not already there.
        val pluralFirstWord = if (firstWord.endsWith("s")) {
            firstWord
        } else {
            firstWord + "s"
        }

        return "$pluralFirstWord ${words.drop(1).joinToString(" ")}".trim()
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