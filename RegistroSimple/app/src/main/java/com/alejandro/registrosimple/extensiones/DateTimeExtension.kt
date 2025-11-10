package com.alejandro.registrosimple.extensiones

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Try to parse form a string to a LocalDate from the default pattern of dd/MM/yyyy
 * If it cante, return null.
 */
fun String.tryParseToLocalDate(format: String = "dd/MM/yyyy"): LocalDate? {
    var dateFormatted: LocalDate? = null
    try {
        dateFormatted = LocalDate.parse(this,
            DateTimeFormatter.ofPattern(format))
    } catch (ex: Exception) {}
    return dateFormatted
}