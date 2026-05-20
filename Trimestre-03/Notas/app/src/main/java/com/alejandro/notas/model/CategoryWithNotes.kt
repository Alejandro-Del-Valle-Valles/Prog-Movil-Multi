package com.alejandro.notas.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * Data class to query a Category along with its associated Notes.
 */
data class CategoryWithNotes(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "name",
        entityColumn = "id",
        associateBy = Junction(NoteCategoryCrossRef::class)
    )
    val notes: List<Note>
)
