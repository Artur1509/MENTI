package com.example.menti.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.menti.databinding.FragmentSignupBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Bottom Navbar
        val navBar = requireActivity().findViewById<BottomNavigationView>(com.example.menti.R.id.bottomNavigation)

        // Zurück zum Login Fragment
        binding.backBTN.setOnClickListener {
            findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToLoginFragment())
        }

        //Neuen Account erstellen
        binding.createAccountBTN.setOnClickListener {
            val email = binding.signupEmailInputET.text.toString()
            val password = binding.signupRepeatPasswordET.text.toString()

            authViewModel.signUp(email, password)

        }

        //Prüfe ob User eingeloggt ist
        authViewModel.user.observe(viewLifecycleOwner){
            if(it != null){
                findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToHomeFragment())
                navBar.visibility = View.VISIBLE
            }
        }

    }
}