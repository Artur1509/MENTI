package com.example.menti.data.model

data class PsychologistProfile(

    var id: Int ?= null,
    var titel: String ?= null,
    var vorname: String ?= null,
    var name: String ?= null,
    var bewertung: Float ?= null,
    var beruf: String ?= null,
    var bild: String?= null,
    var tags: ArrayList<String>?= null

)
