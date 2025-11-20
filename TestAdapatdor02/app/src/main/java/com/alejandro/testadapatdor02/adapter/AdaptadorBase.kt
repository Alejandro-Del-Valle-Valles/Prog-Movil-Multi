package com.alejandro.testadapatdor02.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.alejandro.testadapatdor02.R
import com.alejandro.testadapatdor02.model.Datos
import android.widget.TextView

class AdaptadorBase(
    private val context: Context,
    private val datos: Array<Datos>
): BaseAdapter() {
    override fun getCount(): Int = datos.size

    override fun getItem(position: Int): Any? = datos[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        val inflater = LayoutInflater.from(context)
        val elemento = inflater.inflate(R.layout.vistaelemento, parent, false)
        val texto1 = elemento.findViewById<TextView>(R.id.tvOne)
        val texto2 = elemento.findViewById<TextView>(R.id.tvTwo)
        val dato = datos[position]
        texto1.text = dato.text1
        texto2.text = dato.text2
        return elemento
    }

}