package com.example.menti.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.menti.databinding.FragmentLoginBinding
import com.example.menti.FirebaseViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    val firebaseViewModel: FirebaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bottom Navbar unsichtbar
        val navBar =
            requireActivity().findViewById<BottomNavigationView>(com.example.menti.R.id.bottomNavigation)
        navBar.visibility = View.GONE

        // Navigation zum Signup Fragment
        binding.signupBTN.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
        }

        // Einloggen
        binding.loginBTN.setOnClickListener {

            val email = binding.emailinputET.text.toString()
            val password = binding.passwordInputET.text.toString()

            firebaseViewModel.signIn(email, password, this)
        }


        // Google Login
        binding.googleLoginBTN.setOnClickListener {

        }

        // Prüfe ob User eingeloggt ist
        firebaseViewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                navBar.visibility = View.VISIBLE
            }
        }

    }

}