package com.example.menti.ui

import android.content.ContentValues.TAG
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.caverock.androidsvg.SVG
import com.example.menti.FirebaseViewModel
import com.example.menti.R
import com.example.menti.data.model.PsychologistProfile
import com.example.menti.databinding.FragmentSearchBinding
import com.example.menti.util.SearchResultAdapter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.DocumentReference


class SearchFragment : Fragment() {

    private lateinit var searchRV: RecyclerView
    private lateinit var rvAdapter: SearchResultAdapter
    private lateinit var binding: FragmentSearchBinding
    private lateinit var mapView: MapView
    val firebaseViewModel: FirebaseViewModel by activityViewModels()

    private var googleMap: GoogleMap? = null


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

        binding.toFilterBTN.setOnClickListener {
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToFilterFragment())
        }

        // Daten werden geladen und gefiltert
        loadDataFromFirestoreAndInitializeAdapter()


        // Google Maps
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)


        mapView.getMapAsync { map ->
            googleMap = map
            // Hier kannst du die Google Maps-Operationen durchführen

            try {
                // Stil aus der JSON-Datei laden
                val success = googleMap!!.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style)
                )

                if (!success) {
                    Log.e(TAG, "Style parsing failed.")
                }
            } catch (e: Resources.NotFoundException) {
                Log.e(TAG, "Can't find style. Error: ", e)
            }

            // Marker für Suchergebnisse auf der Map erstellen

           for(i in rvAdapter.dataset) {
               val latLng = LatLng(i.second.standort!!.latitude, i.second.standort!!.longitude)

               val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.locations)
               val width = 72
               val height = 72
               val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, false)

               val markerOptions = MarkerOptions()
                   .position(latLng)
                   .title("${i.second.titel} ${i.second.vorname} ${i.second.name}")
                   .snippet("${i.second.beruf}")

               markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap))

               val marker = googleMap!!.addMarker(markerOptions)

               val builder = LatLngBounds.builder()
               builder.include(marker!!.position)
               val bounds = builder.build()

               val padding = 5 // Hier kannst du einen Padding-Wert in Pixeln festlegen, um den Marker zu umgeben
               val minZoom = 10.0f // Mindestzoomstufe
               val maxZoom = 15.0f // Maximale Zoomstufe

               val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
               googleMap!!.moveCamera(cameraUpdate)
               googleMap!!.animateCamera(CameraUpdateFactory.zoomTo(Math.min(maxZoom, Math.max(minZoom, googleMap!!.cameraPosition.zoom))))
           }


        }

    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()

    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
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