package com.example.menti.data

import com.example.menti.data.model.TerminDaten
import com.example.menti.data.model.Uhrzeit

class Termine {

    fun loadTermine(): List<TerminDaten> {

        return mutableListOf(
            TerminDaten("Montag, 27.11.2023", listOf(Uhrzeit("09:00", false), Uhrzeit("11:00", false), Uhrzeit("14:00", false))),
            TerminDaten("Mittwoch, 29.11.2023", listOf(Uhrzeit("09:00", false))),
            TerminDaten("Donnerstag, 07.12.2023", listOf(Uhrzeit("09:00", false))),
            TerminDaten("Freitag, 15.12.2023", listOf(Uhrzeit("09:00", false))),
        )
    }
}