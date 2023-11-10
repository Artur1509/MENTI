package com.example.menti.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.menti.FirebaseViewModel
import com.example.menti.R
import com.example.menti.data.model.Event
import com.example.menti.data.model.PsychologistProfile
import com.example.menti.databinding.FragmentEventsBinding
import com.example.menti.databinding.FragmentSearchBinding
import com.example.menti.util.EventAdapter
import com.example.menti.util.SearchResultAdapter
import com.google.firebase.firestore.DocumentReference

class EventsFragment : Fragment() {

    private lateinit var binding: FragmentEventsBinding
    val firebaseViewModel: FirebaseViewModel by activityViewModels()

    private lateinit var eventsRV: RecyclerView
    private lateinit var rvAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventsRV = binding.eventsRV
        loadDataFromFirestoreAndInitializeAdapter()
    }


    fun loadDataFromFirestoreAndInitializeAdapter() {
        firebaseViewModel.firestore.collection("Profile")
            .document(firebaseViewModel.user.value!!.email!!)
            .collection("Events")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val loadedData: MutableList<Event> = mutableListOf()

                for (document in querySnapshot) {
                    val documentId = document.id
                    val event = document.toObject(Event::class.java)
                    event.id = documentId
                    loadedData.add(event)
                }

                // Adapter wird erstellt und mit den neuen Daten initialisiert
                rvAdapter = EventAdapter(loadedData, firebaseViewModel, requireContext())
                eventsRV.adapter = rvAdapter
            }
    }

}