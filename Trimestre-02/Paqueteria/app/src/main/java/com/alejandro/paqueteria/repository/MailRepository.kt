package com.alejandro.paqueteria.repository

object MailRepository {

    private var numberOfCertificatedLetters = 0
    fun incrementNumberOfLetters() {
        numberOfCertificatedLetters++
    }

    fun getNumberOfCertificatedLetters(): Int = numberOfCertificatedLetters
}