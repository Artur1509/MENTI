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
import com.example.menti.data.model.PsychologistProfile
import com.example.menti.databinding.FragmentSearchBinding
import com.example.menti.util.searchResultAdapter
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import java.util.EventListener

class SearchFragment : Fragment() {

    private lateinit var searchRV: RecyclerView
    private lateinit var dataset: ArrayList<PsychologistProfile>
    private lateinit var rvAdapter: searchResultAdapter

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

        searchRV = binding.searchResultsRV
        searchRV.setHasFixedSize(true)
        dataset = arrayListOf()
        rvAdapter = searchResultAdapter(dataset)

        searchRV.adapter = rvAdapter

        EventChangeListener()


    }

    private fun EventChangeListener() {

        firebaseViewModel.firestore.collection("PsychologenProfile")
            .addSnapshotListener(object : EventListener,
                com.google.firebase.firestore.EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if(dc.type == DocumentChange.Type.ADDED ) {
                            dataset.add(dc.document.toObject(PsychologistProfile::class.java))
                        }
                    }

                    rvAdapter.notifyDataSetChanged()

                }

            })




    }

}