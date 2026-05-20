package com.alejandro.notas.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
data class Category(
    @PrimaryKey val name: String,
    val color: String = "#828282",
) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Category) return false
            return name == other.name
        }

        override fun hashCode(): Int {
            return name.hashCode()
        }
}
