package com.alejandro.notas.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Cross-reference table for the Many-to-Many relationship between Notes and Categories.
 */
@Entity(
    tableName = "note_category_cross_ref",
    primaryKeys = ["id", "name"],
    foreignKeys = [
        ForeignKey(
            entity = Note::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["name"],
            childColumns = ["name"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("name")]
)
data class NoteCategoryCrossRef(
    val id: Int,
    val name: String
)
