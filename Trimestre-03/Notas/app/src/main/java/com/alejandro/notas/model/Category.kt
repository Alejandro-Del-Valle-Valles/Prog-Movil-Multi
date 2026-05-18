package com.alejandro.notas.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
data class Category(
    @PrimaryKey val name: String,
    val color: String = "#828282",
)
