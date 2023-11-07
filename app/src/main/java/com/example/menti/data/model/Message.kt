package com.example.menti.data.model

data class Message(
    val absender: String,
    val empfaenger: String,
    val datum: String,
    val uhrzeit: String,
    val nachricht: String
){
    constructor() : this("", "", "", "", "")
}
