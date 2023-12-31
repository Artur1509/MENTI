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
import com.example.menti.databinding.FragmentDetailBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

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
        //Argumente die auf die Detailview übergeben werden
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
        val navBar =
            requireActivity().findViewById<BottomNavigationView>(com.example.menti.R.id.bottomNavigation)
        navBar.visibility = View.GONE

        // Zurück zum vorherigen Fragment
        binding.backToSearchBTN.setOnClickListener {
            findNavController().popBackStack()
            navBar.visibility = View.VISIBLE
        }

        // Angaben im Detailprofil
        val imgUri = profilePicture!!.toUri().buildUpon().scheme("https").build()
        binding.detailProfileIV.load(imgUri)

        binding.detailNameTV.text = "${titel!!} ${vorname!!} ${name!!}"
        binding.detailBerufTV.text = beruf!!
        binding.detailRatingRB.rating = bewertung!!
        binding.beschreibungTV.text = beschreibung!!
        // Chips werden erstellt
        tags.forEach {
            binding.detailTagsCG.addChip(it)
        }


        binding.terminBTN.setOnClickListener {
            findNavController().navigate(
                DetailFragmentDirections.actionDetailFragmentToLeistungenFragment(
                    profilePicture = profilePicture!!,
                    vorname = vorname!!,
                    name = name!!,
                    beruf = beruf!!,
                    titel = titel!!,
                    bewertung = bewertung!!
                )
            )
        }

        // Chips die mit den Suchbegriffen im Filter übereinstimmen werden farblich hervorgehoben.
        var chipGroup = binding.detailTagsCG

        try {

            if (firebaseViewModel.selectedFilter.value!!.isNotEmpty()) {


                firebaseViewModel.selectedFilter.value!!.forEach {

                    val chip = chipGroup.getChildAt(tags.indexOf(it)) as Chip
                    chip.setChipStrokeColorResource(R.color.accent)
                    chip.setTextColor(
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.accent
                            )
                        )
                    )

                }

            }
        } catch (e: Exception) {
            Log.e("chips", e.message.toString())
        }

        binding.sendMessageBTN.setOnClickListener {

            firebaseViewModel.firestore.collection("Profile")
                .document(firebaseViewModel.user.value!!.email!!)
                .get()
                .addOnSuccessListener {

                    val absenderVorname = it.data!!["vorname"]
                    val absenderName = it.data!!["name"]
                    val absenderId = it.id

                    firebaseViewModel.createChat(
                        absenderName = "${absenderVorname} ${absenderName}",
                        absenderId = "${absenderId}",
                        empfaengerName = "${titel} ${vorname} ${name}",
                        empfaengerId = "${vorname}${name}id",
                        chatId = "ChatVon${absenderVorname}und${vorname}"
                    )
                }

            findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToMessengerFragment())

        }


    }

    // Style der Chips im Detailfragment
    fun ChipGroup.addChip(label: String) {
        Chip(context).apply {

            id = View.generateViewId()
            text = label
            isClickable = false
            isFocusable = false
            chipBackgroundColor =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.secondary))
            setEnsureMinTouchTargetSize(false)
            addView(this)
        }
    }

}