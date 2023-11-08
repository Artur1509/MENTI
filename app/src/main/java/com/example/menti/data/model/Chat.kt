package com.example.menti.data.model

data class Chat(
    val absenderName: String,
    val absenderId: String,
    val empfaengerName: String,
    val empfaengerId: String,
    var chatId: String,
    val erstellungsDatum: String,
    val erstellungsZeit: String
) {
    constructor() : this("", "", "", "", "", "", "")
}
