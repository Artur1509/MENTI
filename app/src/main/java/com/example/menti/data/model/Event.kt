package com.example.menti.data.model

data class Event(
    val experte: String,
    val leistung: String,
    val preis: String,
    val datum: String,
    val uhrzeit: String,
    val erstellungsdatum: String,
    val erstellungszeit: String,
    var id: String
) {
    constructor() : this("", "", "", "", "", "", "", "")
}
