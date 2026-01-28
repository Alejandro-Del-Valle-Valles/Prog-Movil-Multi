package com.alejandro.repasorapidobbdd.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alumnos")
class Alumno(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val nombre: String,
    val edad: Int,
) {

    override fun toString(): String {
        return "Alumno(id=$id, nombre='$nombre', edad=$edad)"
    }
}