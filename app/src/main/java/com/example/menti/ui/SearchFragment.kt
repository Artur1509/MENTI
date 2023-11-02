package com.example.menti.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.menti.FirebaseViewModel
import com.example.menti.data.model.PsychologistProfile
import com.example.menti.databinding.FragmentSearchBinding
import com.example.menti.util.SearchResultAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.DocumentReference


class SearchFragment : Fragment() {

    private lateinit var searchRV: RecyclerView
    private lateinit var rvAdapter: SearchResultAdapter
    private lateinit var binding: FragmentSearchBinding
    val firebaseViewModel: FirebaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navbar sichtbarkeit
        val navBar = requireActivity().findViewById<BottomNavigationView>(com.example.menti.R.id.bottomNavigation)
        navBar.visibility = View.VISIBLE

        //Recyclerview
        searchRV = binding.searchResultsRV
        searchRV.setHasFixedSize(true)

        // Daten werden geladen und gefiltert
        loadDataFromFirestoreAndInitializeAdapter()

    }

    // Funktion um Daten aus dem Firestore zu laden und zu filtern, wenn ein filter aktiviert ist.
    fun loadDataFromFirestoreAndInitializeAdapter() {
        firebaseViewModel.firestore.collection("PsychologenProfile")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val loadedData: MutableList<Pair<DocumentReference, PsychologistProfile>> = mutableListOf()

                for (document in querySnapshot) {
                    val pair = Pair<DocumentReference, PsychologistProfile>(
                        document.reference,
                        document.toObject(PsychologistProfile::class.java)
                    )
                    loadedData.add(pair)
                }
                //Adapter wird erstellt und mit den neuen daten initialisiert
                rvAdapter = SearchResultAdapter(loadedData, firebaseViewModel, requireContext())
                searchRV.adapter = rvAdapter

                //Ausführung des Filters, wenn die variable in der die begriffe gespeichert werden nicht leer ist.
                firebaseViewModel.selectedFilter.value?.let { selectedFilter ->
                    if (selectedFilter.isNotEmpty()) {
                        rvAdapter.filter(selectedFilter)
                    }
                }
            }
    }
}