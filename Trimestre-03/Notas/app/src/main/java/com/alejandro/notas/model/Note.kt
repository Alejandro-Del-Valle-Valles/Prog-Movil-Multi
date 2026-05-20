package com.alejandro.notas.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "notas")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String?,
    val color: String = "#FFC369",
    val editedAt: LocalDateTime = LocalDateTime.now()
)
