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

        binding.toZahlungBTN.setOnClickListener {
            findNavController().navigate(PersoenlicheAngabenFragmentDirections.actionPersoenlicheAngabenFragmentToZahlungsartenFragment())
        }
    }
}
