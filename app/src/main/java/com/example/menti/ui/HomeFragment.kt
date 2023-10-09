package com.example.menti.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.menti.FirebaseViewModel
import com.example.menti.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeFragment : Fragment() {


    private lateinit var binding: FragmentHomeBinding
    val firebaseViewModel: FirebaseViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navBar = requireActivity().findViewById<BottomNavigationView>(com.example.menti.R.id.bottomNavigation)
        navBar.visibility = View.VISIBLE

        binding.searchBTN.setOnClickListener{
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFilterFragment())
        }

        binding.logoutBTN.setOnClickListener {
            firebaseViewModel.signOut()
        }

        binding.toProfileBTN.setOnClickListener {

            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment())

        }

        // Prüfe ob User eingeloggt ist, wenn nicht -> Navigation zum LoginFragment
        firebaseViewModel.user.observe(viewLifecycleOwner) {
            if(it == null) {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
            }
        }

    }

}