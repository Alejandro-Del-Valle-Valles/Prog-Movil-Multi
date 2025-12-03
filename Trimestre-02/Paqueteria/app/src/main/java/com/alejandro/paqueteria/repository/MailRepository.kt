package com.alejandro.paqueteria.repository

object MailRepository {

    private var numberOfLetters = 0
    private var numberOfPackages = 0

    fun incrementNumberOfLetters() {
        numberOfLetters++
    }

    fun incrementNumberOfPackages() {
        numberOfPackages++
    }
}