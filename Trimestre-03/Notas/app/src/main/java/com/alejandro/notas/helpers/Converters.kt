package com.alejandro.notas.helpers

import androidx.room.TypeConverter
import java.time.LocalDateTime

/**
 * Converters for Room to handle non-primitive data types.
 */
class Converters {

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}