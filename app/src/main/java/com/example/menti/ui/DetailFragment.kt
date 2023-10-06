package com.example.menti.ui

import android.os.Bundle
import android.util.Log
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
import com.example.menti.data.model.PsychologistProfile
import com.example.menti.databinding.FragmentDetailBinding
import com.example.menti.util.searchResultAdapter

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    val firebaseViewModel: FirebaseViewModel by activityViewModels()

    var profilePicture: String? = ""
    var titel: String? = ""
    var name: String? = ""
    var vorname: String? = ""
    var bewertung: Float? = 0F
    var beruf: String? = ""

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
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            binding.backToSearchBTN.setOnClickListener {
                findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToSearchFragment())
            }

        val imgUri = profilePicture!!.toUri().buildUpon().scheme("https").build()
        binding.detailProfileIV.load(imgUri)

        binding.detailNameTV.text = "${titel!!} ${vorname!!} ${name!!}"
        binding.detailBerufTV.text = beruf!!
        binding.detailRatingRB.rating = bewertung!!

    }

}