package com.example.menti.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.menti.data.Resource
import com.example.menti.databinding.FragmentSignupBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private val viewModel by viewModels<AuthViewModel>()
    private lateinit var binding: FragmentSignupBinding

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

        val navBar = requireActivity().findViewById<BottomNavigationView>(com.example.menti.R.id.bottomNavigation)

        binding.backBTN.setOnClickListener {
            findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToLoginFragment())
        }

        binding.createAccountBTN.setOnClickListener {
            val email = binding.signupEmailInputET.text.toString()
            val password = binding.signupRepeatPasswordET.text.toString()

            viewModel.signUp(email, password)

            viewModel.signUpFlow.observe(viewLifecycleOwner) {
                if(it is Resource.Success){
                    findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToHomeFragment())
                    navBar.visibility = View.VISIBLE
                }
            }
        }

    }
}