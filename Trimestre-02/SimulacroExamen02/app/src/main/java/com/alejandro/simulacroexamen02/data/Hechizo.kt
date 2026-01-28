package com.alejandro.simulacroexamen02.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hechizos")
class Hechizo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val tipo: String,
    val nombre: String,
    val dano: Int,
    val numRunas: Int
) {

    override fun toString(): String {
        return "Tipo del hechizo: $tipo | Nombre: $nombre | Daño inflingido: $dano | Numero de Runas: $numRunas"
    }
}