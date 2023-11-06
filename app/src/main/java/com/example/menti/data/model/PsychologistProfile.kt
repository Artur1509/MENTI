package com.example.menti.data.model

import com.google.firebase.firestore.GeoPoint

data class PsychologistProfile(

    var titel: String ?= null,
    var vorname: String ?= null,
    var name: String ?= null,
    var bewertung: Float ?= null,
    var beruf: String ?= null,
    var bild: String?= null,
    var tags: ArrayList<String>?= null,
    var beschreibung: String?= null,
    var standort: GeoPoint?= null

)
