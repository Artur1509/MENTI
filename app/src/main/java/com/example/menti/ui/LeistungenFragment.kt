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
import com.example.menti.data.Leistungen
import com.example.menti.databinding.FragmentLeistungenBinding
import com.example.menti.util.LeistungenAdapter

class LeistungenFragment : Fragment() {

    private lateinit var binding: FragmentLeistungenBinding
    val firebaseViewModel: FirebaseViewModel by activityViewModels()

    var profilePicture: String? = ""
    var titel: String? = ""
    var name: String? = ""
    var vorname: String? = ""
    var beruf: String? = ""
    var bewertung: Float? = 0F



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Argumente die auf die Detailview übergeben werden
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
        binding = FragmentLeistungenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Recyclerview
        val leistungenRV = binding.leistungenRV
        val data = Leistungen().loadLeistungen()
        leistungenRV.adapter = LeistungenAdapter(data, firebaseViewModel)

        binding.backToDetailBTN.setOnClickListener {
            findNavController().popBackStack()
        }

        val imgUri = profilePicture!!.toUri().buildUpon().scheme("https").build()
        binding.leistungProfilIV.load(imgUri)

        binding.detailName2TV.text = "${titel!!} ${vorname!!} ${name!!}"
        binding.detailBerufTV2.text = beruf!!
        binding.leistungRatingBar.rating = bewertung!!


    }

}