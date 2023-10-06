package com.example.menti.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.menti.FirebaseViewModel
import com.example.menti.R
import com.example.menti.data.model.PsychologistProfile
import com.example.menti.databinding.FragmentDetailBinding
import com.example.menti.util.searchResultAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.protobuf.LazyStringArrayList
import java.util.ArrayList

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    val firebaseViewModel: FirebaseViewModel by activityViewModels()

    var profilePicture: String? = ""
    var titel: String? = ""
    var name: String? = ""
    var vorname: String? = ""
    var bewertung: Float? = 0F
    var beruf: String? = ""
    var beschreibung: String? = ""
    var tags: Array<out String> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            profilePicture = it.getString("profilePicture")
            titel = it.getString("titel")
            name = it.getString("name")
            vorname = it.getString("vorname")
            beruf = it.getString("beruf")
            bewertung = it.getFloat("bewertung")
            beschreibung = it.getString("beschreibung")
            tags = it.getStringArray("tags")!!
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

        // Bottom Navbar unsichtbar
        val navBar = requireActivity().findViewById<BottomNavigationView>(com.example.menti.R.id.bottomNavigation)
        navBar.visibility = View.GONE

        binding.backToSearchBTN.setOnClickListener {
            findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToSearchFragment())
            navBar.visibility = View.VISIBLE
        }

        val imgUri = profilePicture!!.toUri().buildUpon().scheme("https").build()
        binding.detailProfileIV.load(imgUri)

        binding.detailNameTV.text = "${titel!!} ${vorname!!} ${name!!}"
        binding.detailBerufTV.text = beruf!!
        binding.detailRatingRB.rating = bewertung!!
        binding.beschreibungTV.text = beschreibung!!

        tags.forEach {
            binding.detailTagsCG.addChip(it)
        }



    }

    fun ChipGroup.addChip(label: String ) {
        Chip(context).apply {

            id = View.generateViewId()
            text = label
            isClickable = false
            isFocusable = false
            chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.secondary))
            setEnsureMinTouchTargetSize(false)
            addView(this)
        }
    }

}