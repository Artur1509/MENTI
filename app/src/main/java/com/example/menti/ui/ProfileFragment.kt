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
import com.example.menti.databinding.FragmentProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    val firebaseViewModel: FirebaseViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bottom Navbar unsichtbar
        val navBar =
            requireActivity().findViewById<BottomNavigationView>(com.example.menti.R.id.bottomNavigation)
        navBar.visibility = View.GONE

        //Zurück zum Home Fragment
        binding.backToHomeBTN.setOnClickListener {

            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToHomeFragment())
            navBar.visibility = View.VISIBLE
        }
        // Profilinfo Anzeigen
        firebaseViewModel.profileRef.addSnapshotListener { value, error ->
            Log.d("Firebase", "$value , ${value?.data}")
            value?.let {
                binding.profileAnredeET.setText(value["anrede"].toString())
                binding.profileVornameET.setText(value["vorname"].toString())
                binding.profileNameET.setText(value["name"].toString())
                binding.profilePlzET.setText(value["plz"].toString())
                binding.profileOrtET.setText(value["ort"].toString())
                binding.profileAnschriftET.setText(value["anschrift"].toString())
                binding.profileTelefonnummerET.setText(value["telefonnummer"].toString())
            }

        }

        //Profilangaben bearbeiten
        binding.saveChangesBTN.setOnClickListener {

            val anrede = binding.profileAnredeET.text.toString()
            val vorname = binding.profileVornameET.text.toString()
            val name = binding.profileNameET.text.toString()
            val plz = binding.profilePlzET.text.toString()
            val ort = binding.profileOrtET.text.toString()
            val anschrift = binding.profileAnschriftET.text.toString()
            val telefonNummer = binding.profileTelefonnummerET.text.toString()

            firebaseViewModel.editProfile(
                anrede,
                vorname,
                name,
                plz,
                ort,
                anschrift,
                telefonNummer,
                this
            )

        }
    }

}