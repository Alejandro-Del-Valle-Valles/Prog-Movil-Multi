package com.alejandro.recetas.services

import android.content.Context
import com.alejandro.recetas.module.Recipe
import kotlinx.serialization.json.Json

object JsonService {

    /**
     * Return an Array of Recipes, it could be null.
     */
    fun getRecipes(context: Context): Array<Recipe>? {
        var recipes: Array<Recipe>?
        try {
            val json = context.assets.open("recetas.json")
                .bufferedReader().use {it.readText()}
            recipes = Json.decodeFromString<Array<Recipe>>(json)
        } catch (ex: Exception) {
            recipes = null
        }
        return recipes
    }
}