package com.alejandro.paqueteria.model

abstract class Mail(
    val sender: String, val destination: String, val insured: Boolean
) {

    init {
        require(sender.trim() == "") {
            "El remitente no puede estar vacío."
        }
        require(destination.trim() == "") {
            "El destinatario no puede estar vacío."
        }
    }
}