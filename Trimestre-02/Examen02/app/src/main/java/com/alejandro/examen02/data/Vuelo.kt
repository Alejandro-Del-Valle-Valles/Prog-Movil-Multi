package com.alejandro.examen02.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alejandro.examen02.enums.TipoVuelo

@Entity(tableName = "Vuelos")
class Vuelo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val origen: String,
    val destino: String,
    val vuelo: TipoVuelo
) {

}