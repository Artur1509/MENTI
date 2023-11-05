package com.example.menti.data

import com.example.menti.data.model.TerminDaten
import com.example.menti.data.model.Uhrzeit

class Termine {

    fun loadTermine(): List<TerminDaten> {

        return mutableListOf(
            TerminDaten("Montag, 27.11.2023", listOf(Uhrzeit("09:00", false, "Montag, 27.11.2023"), Uhrzeit("11:00", false, "Montag, 27.11.2023"), Uhrzeit("14:00", false, "Montag, 27.11.2023"))),
            TerminDaten("Mittwoch, 29.11.2023", listOf(Uhrzeit("09:00", false, "Mittwoch, 29.11.2023"))),
            TerminDaten("Donnerstag, 07.12.2023", listOf(Uhrzeit("09:00", false, "Donnerstag, 07.12.2023" ))),
            TerminDaten("Freitag, 15.12.2023", listOf(Uhrzeit("09:00", false, "Freitag, 15.12.2023"))),
        )
    }

    //Filtert die Liste der Termindaten nach dem Element welches bei der Uhrzeit isChecked = true hat.
    fun filterCheckedTerminDaten(termine: List<TerminDaten>): List<TerminDaten> {
        return termine.filter { termin -> termin.uhrzeit.any { it.isChecked } }
    }

}