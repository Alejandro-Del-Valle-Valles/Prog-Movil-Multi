package com.alejandro.simulacroexamen01.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Unidades")
class Tropa(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val nombre: String,
    val faccion: String,
    val fuerza: Int
) {

    override fun toString(): String {
        return "Nombre de la tropa: '$nombre' | Facción: '$faccion' | Fuera: $fuerza"
    }
}