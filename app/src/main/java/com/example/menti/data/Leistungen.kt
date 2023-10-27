package com.example.menti.data

import com.example.menti.data.model.Leistung

class Leistungen {

    fun loadLeistungen(): List<Leistung> {

        return mutableListOf(
            Leistung("Erstgespräch (Einzelsitzung)", "89,00 €", false),
            Leistung("Einzeltherapie", "89,00 €", false),
            Leistung("Coaching (Einzelsitzung)", "89,00 €", false),
            Leistung("Paartherapie", "129,00 €", false),
        )
    }
}