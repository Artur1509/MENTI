package com.example.menti.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.menti.FirebaseViewModel
import com.example.menti.R
import com.example.menti.data.model.PsychologistProfile
import com.example.menti.databinding.FragmentZahlungsartenBinding
import com.example.menti.databinding.FragmentZahlungsuebersichtBinding
import com.example.menti.util.SearchResultAdapter
import com.google.firebase.firestore.DocumentReference

class ZahlungsuebersichtFragment : Fragment() {

    private lateinit var binding: FragmentZahlungsuebersichtBinding
    val firebaseViewModel: FirebaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentZahlungsuebersichtBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backToZahlungsartenBTN.setOnClickListener {
            findNavController().popBackStack()
        }

        loadProfileDataFromFirestore()

        binding.artDesTerminsTV.text = firebaseViewModel.leistungAuswahl.value!!.beschreibung
        binding.therapiePreisTV.text = firebaseViewModel.leistungAuswahl.value!!.preis

        binding.buchenBTN.setOnClickListener {
            val experte = firebaseViewModel.experteAuswahl.value!!
            val leistung = firebaseViewModel.leistungAuswahl.value!!
            var termin = firebaseViewModel.terminAuswahl.value!!
            firebaseViewModel.createEvent(experte, leistung, termin)
            firebaseViewModel.createEventNotification()

            findNavController().navigate(ZahlungsuebersichtFragmentDirections.actionZahlungsuebersichtFragmentToBuchungsbestaetigungFragment())
        }


    }

    fun loadProfileDataFromFirestore() {

        firebaseViewModel.firestore.collection("Profile")
            .document(firebaseViewModel.user.value!!.email!!)
            .get()
            .addOnSuccessListener {

                val vorname = it.data!!["vorname"]
                val name = it.data!!["name"]
                val plz = it.data!!["plz"]
                val ort = it.data!!["ort"]
                val anschrift = it.data!!["anschrift"]

                binding.vornameNachnameTV.text = "${vorname} ${name}"
                binding.adresseTV.text = "${anschrift}"
                binding.plzOrtTV.text = "${plz}, ${ort}"

            }

    }

}