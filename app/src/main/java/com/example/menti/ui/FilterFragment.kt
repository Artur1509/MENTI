package com.example.menti.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.menti.FirebaseViewModel
import com.example.menti.data.Categories
import com.example.menti.databinding.FragmentFilterBinding
import com.example.menti.util.FilterCategoriesAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class FilterFragment : Fragment() {

    private lateinit var binding: FragmentFilterBinding
    val firebaseViewModel: FirebaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Recyclerview
        val categoriesRV = binding.filterCategoriesRV
        val data = Categories().loadCategories()
        categoriesRV.adapter = FilterCategoriesAdapter(data, firebaseViewModel)

        //Navbar unsichtbar
        val navBar = requireActivity().findViewById<BottomNavigationView>(com.example.menti.R.id.bottomNavigation)
        navBar.visibility = View.GONE

        // Zurück zum Homescreen
        binding.filterToHomeBTN.setOnClickListener {
            findNavController().navigate(FilterFragmentDirections.actionFilterFragmentToHomeFragment())
        }

        // Weiter zu den Suchergebnissen
        binding.toSearchResultsBTN.setOnClickListener {
            findNavController().navigate(FilterFragmentDirections.actionFilterFragmentToSearchFragment())
            firebaseViewModel.selectFilterOptions(data)
            Log.e("Filter", data.toString() )
        }


    }

}