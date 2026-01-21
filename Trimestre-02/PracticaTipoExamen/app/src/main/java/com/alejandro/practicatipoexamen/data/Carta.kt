package com.alejandro.practicatipoexamen.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cartas")
data class Carta(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val remitente: String,
    val contenido: String,
    val destinatario: String) {
}