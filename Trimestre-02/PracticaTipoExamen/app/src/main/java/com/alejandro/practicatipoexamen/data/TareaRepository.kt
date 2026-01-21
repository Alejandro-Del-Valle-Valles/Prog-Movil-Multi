package com.alejandro.practicatipoexamen.data

import androidx.lifecycle.LiveData

class TareaRepository(private val daoCarta: DaoCarta) {
    val cartas: LiveData<List<Carta>> = daoCarta.getCartas()

    suspend fun inserta(carta: Carta) {
        daoCarta.insert(carta)
    }

    suspend fun delete(carta: Carta) {
        daoCarta.delete(carta)
    }

    suspend fun deleteAll() {
        daoCarta.deleteCartas()
    }

    //TODO: Implementar esto
}