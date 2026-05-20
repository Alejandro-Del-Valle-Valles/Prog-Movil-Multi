package com.alejandro.notas.helpers

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alejandro.notas.R
import com.google.android.material.card.MaterialCardView

class ColorAdapter(
    private val colors: List<String>,
    private val onColorSelected: (String) -> Unit
) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    class ColorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardColor = view.findViewById<MaterialCardView>(R.id.cardColor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_color_picker, parent, false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val colorHex = colors[position]
        holder.cardColor.setCardBackgroundColor(Color.parseColor(colorHex))
        holder.itemView.setOnClickListener { onColorSelected(colorHex) }
    }

    override fun getItemCount() = colors.size
}