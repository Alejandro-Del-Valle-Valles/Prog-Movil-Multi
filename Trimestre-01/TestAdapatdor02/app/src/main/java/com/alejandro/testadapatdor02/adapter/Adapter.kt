package com.alejandro.testadapatdor02.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.alejandro.testadapatdor02.R
import com.alejandro.testadapatdor02.model.Datos

class Adapter(
    context: Context,
    private val datos: Array<Datos>
): ArrayAdapter<Datos>(context, R.layout.vistaelemento, datos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val elemento = inflater.inflate(R.layout.vistaelemento, parent, false)

        val textOne = elemento.findViewById<TextView>(R.id.tvOne)
        textOne.text = datos[position].text1

        val textTwo = elemento.findViewById<TextView>(R.id.tvTwo)
        textTwo.text = datos[position].text2

        return elemento
    }
}