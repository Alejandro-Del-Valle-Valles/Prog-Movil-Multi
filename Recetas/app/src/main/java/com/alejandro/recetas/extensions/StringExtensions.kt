package com.alejandro.recetas.extensions

/**
 * Capitalize each word of a string
 */
fun String.capitalizeWords(): String {
    return this.split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}

/**
 * Capitalize the first word of a string
 */
fun String.capitalize(): String = this.trim().first().uppercaseChar() +
        this.trim().substring(1).lowercase()

/**
 * Add a char at the end of each word if the last char of the word isn't the same.
 * It isn't case sensitive.
 */
fun String.addCharAtEndToWords(char: Char): String {
    val words = this.trim().split(" ").toMutableList()
    words.forEachIndexed { index, string ->
        if(string.trim().last().lowercase() != char.lowercase()) words[index] = string + char
    }
    return words.joinToString(" ")
}