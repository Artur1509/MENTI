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
import com.example.menti.databinding.FragmentFavoritesBinding
import com.example.menti.util.FavoritesAdapter
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import java.util.EventListener

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var dataset: ArrayList<Pair<String, PsychologistProfile>>
    private lateinit var rvAdapter: FavoritesAdapter
    private lateinit var favoritesRV: RecyclerView

    val firebaseViewModel: FirebaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recyclerview
        favoritesRV = binding.favoritesRV
        favoritesRV.setHasFixedSize(true)
        dataset = arrayListOf()
        rvAdapter = FavoritesAdapter(dataset, firebaseViewModel)
        favoritesRV.adapter = rvAdapter
        eventChangeListener()
        Log.e("RV", "${dataset}")



    }

    // Profile werden aus dem Firestore ins Dataset geladen
    private fun eventChangeListener() {

        firebaseViewModel.firestore.collection("Profile")
            .document(firebaseViewModel.user.value!!.email!!).collection("Favoriten")
            .addSnapshotListener(object : EventListener,
                com.google.firebase.firestore.EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            var id = dc.document.id
                            var favorit = dc.document.data["reference"] as DocumentReference
                            var favoritRef = favorit.get().addOnSuccessListener {snapshot ->
                                var profil = snapshot.toObject(PsychologistProfile::class.java)!!
                                dataset.add(Pair(id, profil))
                                //Log.e("RV", "${profil.tags}")
                                rvAdapter.updateData(dataset)

                            }

                        }
                    }


                }
            })
    }
}