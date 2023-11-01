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
import com.example.menti.data.model.Category
import com.example.menti.data.model.PsychologistProfile
import com.example.menti.databinding.FragmentSearchBinding
import com.example.menti.util.SearchResultAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import java.util.EventListener

class SearchFragment : Fragment() {

    private lateinit var searchRV: RecyclerView
    private var dataset: ArrayList<Pair<DocumentReference, PsychologistProfile>> = arrayListOf()
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

        Log.e("Filter", firebaseViewModel.selectedFilter.value.toString())

        //Navbar sichtbarkeit
        val navBar = requireActivity().findViewById<BottomNavigationView>(com.example.menti.R.id.bottomNavigation)
        navBar.visibility = View.VISIBLE

        // Recyclerview
        searchRV = binding.searchResultsRV
        searchRV.setHasFixedSize(true)
        //dataset = arrayListOf()
        rvAdapter = SearchResultAdapter(dataset, firebaseViewModel)
        searchRV.adapter = rvAdapter

        // Filter der RV
        firebaseViewModel.selectedFilter.observe(viewLifecycleOwner) { selectedFilter ->
            rvAdapter.filter(selectedFilter)
            Log.e("filter", selectedFilter.toString())
        }
        eventChangeListener()




    }


    // Profile werden aus dem Firstore ins Dataset geladen
    private fun eventChangeListener() {

        firebaseViewModel.firestore.collection("PsychologenProfile")
            .addSnapshotListener(object : EventListener,
                com.google.firebase.firestore.EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                    for (dc: DocumentChange in value?.documentChanges!!) {

                        if(dc.type == DocumentChange.Type.ADDED ) {
                            val pair = Pair<DocumentReference, PsychologistProfile>(
                                dc.document.reference,
                                dc.document.toObject(PsychologistProfile::class.java
                            ))
                            dataset.add(pair)
                        }
                    }

                    rvAdapter.notifyDataSetChanged()

                }
            })




    }

}