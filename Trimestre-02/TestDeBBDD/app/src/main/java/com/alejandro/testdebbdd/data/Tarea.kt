package com.alejandro.testdebbdd.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tarea(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val titulo: String,
    val completada: Boolean = false
)
