package com.example.menti.data.model

import java.security.Timestamp

data class Message(
    val absender: String,
    val empfaenger: String,
    val erstellungsDatum: String,
    val erstellungsZeit: String,
    val message: String,
    val timeStamp: String,
){
    constructor() : this("", "", "", "", "","")
}
