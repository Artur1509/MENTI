package com.example.menti.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.menti.R
import com.example.menti.databinding.FragmentProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bottom Navbar unsichtbar
        val navBar = requireActivity().findViewById<BottomNavigationView>(com.example.menti.R.id.bottomNavigation)
        navBar.visibility = View.GONE

        binding.backToHomeBTN.setOnClickListener {

            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToHomeFragment())
            navBar.visibility = View.VISIBLE
        }
    }

}