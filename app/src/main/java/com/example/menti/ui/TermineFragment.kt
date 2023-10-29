package com.example.menti.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.menti.FirebaseViewModel
import com.example.menti.R
import com.example.menti.data.Categories
import com.example.menti.data.Termine
import com.example.menti.databinding.FragmentLeistungenBinding
import com.example.menti.databinding.FragmentTermineBinding
import com.example.menti.util.FilterCategoriesAdapter
import com.example.menti.util.TermineAdapter

class TermineFragment : Fragment() {

    private lateinit var binding: FragmentTermineBinding
    val firebaseViewModel: FirebaseViewModel by activityViewModels()

    var profilePicture: String? = ""
    var titel: String? = ""
    var name: String? = ""
    var vorname: String? = ""
    var beruf: String? = ""
    var bewertung: Float? = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            profilePicture = it.getString("profilePicture")
            titel = it.getString("titel")
            name = it.getString("name")
            vorname = it.getString("vorname")
            beruf = it.getString("beruf")
            bewertung = it.getFloat("bewertung")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTermineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val termineRV = binding.termineRV
        val data = Termine().loadTermine()
        termineRV.adapter = TermineAdapter(data, firebaseViewModel, requireContext())

        val imgUri = profilePicture!!.toUri().buildUpon().scheme("https").build()
        binding.termineProfileIV.load(imgUri)
        binding.termineProfileNameTV.text = "${titel!!} ${vorname!!} ${name!!}"
        binding.termineProfileBerufTV.text = beruf
        binding.termineRB.rating = bewertung!!

        binding.backToLeistungenBTN.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}