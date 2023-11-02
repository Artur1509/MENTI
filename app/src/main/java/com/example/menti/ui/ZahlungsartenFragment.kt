package com.example.menti.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.menti.FirebaseViewModel
import com.example.menti.R
import com.example.menti.databinding.FragmentPersoenlicheangabenBinding
import com.example.menti.databinding.FragmentZahlungsartenBinding

class ZahlungsartenFragment : Fragment() {

    private lateinit var binding: FragmentZahlungsartenBinding
    val firebaseViewModel: FirebaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentZahlungsartenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val checkbox = binding.rechnungCB
        checkbox.isChecked

        binding.backToPersoenlicheAngabenBTN.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}