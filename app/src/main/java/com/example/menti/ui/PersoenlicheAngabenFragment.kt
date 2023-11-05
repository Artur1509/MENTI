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
import com.example.menti.databinding.FragmentPersoenlicheangabenBinding
import com.example.menti.databinding.FragmentTermineBinding

class PersoenlicheAngabenFragment : Fragment() {

    private lateinit var binding: FragmentPersoenlicheangabenBinding
    val firebaseViewModel: FirebaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersoenlicheangabenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backToTermineBTN.setOnClickListener {
            findNavController().popBackStack()
        }

        firebaseViewModel.profileRef.addSnapshotListener { value, error ->
            Log.d("Firebase", "$value , ${value?.data}")
            value?.let {
                binding.rechnungVornameET.setText(value["vorname"].toString())
                binding.rechnungNameET.setText(value["name"].toString())
                binding.rechnungPlzET.setText(value["plz"].toString())
                binding.rechnungOrtET.setText(value["ort"].toString())
                binding.rechnungAnschriftET.setText(value["anschrift"].toString())
            }

        }

        binding.toZahlungBTN.setOnClickListener {
            findNavController().navigate(PersoenlicheAngabenFragmentDirections.actionPersoenlicheAngabenFragmentToZahlungsartenFragment())

            val vorname = binding.rechnungVornameET.text.toString()
            val name = binding.rechnungNameET.text.toString()
            val plz = binding.rechnungPlzET.text.toString()
            val ort = binding.rechnungOrtET.text.toString()
            val anschrift = binding.rechnungAnschriftET.text.toString()


            firebaseViewModel.editRechnungsAdresse(vorname, name, plz, ort, anschrift, this)
        }

    }
}
