package com.alejandro.notas.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * Data class to query a Note along with its associated Categories.
 */
data class NoteWithCategories(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "id",
        entityColumn = "name",
        associateBy = Junction(NoteCategoryCrossRef::class)
    )
    val categories: List<Category>
)
