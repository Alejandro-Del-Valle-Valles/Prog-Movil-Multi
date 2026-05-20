package com.alejandro.notas.helpers

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.alejandro.notas.R
import com.alejandro.notas.model.NoteWithCategories
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class NoteAdapter(
    private var items: List<NoteWithCategories> = emptyList(),
    private val onNoteClick: (NoteWithCategories) -> Unit = {}
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card = view.findViewById<MaterialCardView>(R.id.cardNote)
        val title = view.findViewById<TextView>(R.id.tvNoteTitle)
        val content = view.findViewById<TextView>(R.id.tvNoteContent)
        val chipGroup = view.findViewById<ChipGroup>(R.id.cgCategories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = items[position]
        val note = item.note

        holder.title.text = note.title
        holder.content.text = note.content
        holder.card.setCardBackgroundColor(Color.parseColor(note.color))
        holder.itemView.setOnClickListener { onNoteClick(item) }

        holder.chipGroup.removeAllViews()
        item.categories.forEach { category ->
            val chip = Chip(holder.itemView.context).apply {
                text = category.name
                chipBackgroundColor = ColorStateList.valueOf(Color.parseColor(category.color))
                setTextColor(Color.WHITE)
                textSize = 12f
            }
            holder.chipGroup.addView(chip)
        }
    }

    override fun getItemCount() = items.size

    fun updateNotes(newItems: List<NoteWithCategories>) {
        this.items = newItems
        notifyDataSetChanged()
    }

}