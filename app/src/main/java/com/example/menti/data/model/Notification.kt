package com.example.menti.data.model

data class Notification(
    val message: String,
    val erstellungsdatum: String,
    val erstellungszeit: String,
    val typ: String

) {
    constructor() : this("", "", "", "")
}
