package com.example.menti.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.menti.FirebaseViewModel
import com.example.menti.data.model.Event
import com.example.menti.data.model.Notification
import com.example.menti.databinding.FragmentHomeBinding
import com.example.menti.util.EventAdapter
import com.example.menti.util.NotificationsAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeFragment : Fragment() {


    private lateinit var binding: FragmentHomeBinding
    val firebaseViewModel: FirebaseViewModel by activityViewModels()
    private lateinit var notificationsRV: RecyclerView
    private lateinit var rvAdapter: NotificationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        notificationsRV = binding.notificationsRV

        //Navbar Sichtbarkeit
        val navBar = requireActivity().findViewById<BottomNavigationView>(com.example.menti.R.id.bottomNavigation)
        navBar.visibility = View.VISIBLE
        navBar.menu.getItem(2).isChecked = true

        // Zum Filterfragment
        binding.searchBTN.setOnClickListener{
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFilterFragment())
        }

        // Ausloggen
        binding.logoutBTN.setOnClickListener {
            firebaseViewModel.signOut()
        }

        // Zum Profilangaben Fragment
        binding.toProfileBTN.setOnClickListener {

            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment())

        }

        // Prüfe ob User eingeloggt ist, wenn nicht -> Navigation zum LoginFragment
        firebaseViewModel.user.observe(viewLifecycleOwner) {
            if(it == null) {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
            }
        }

        loadDataFromFirestoreAndInitializeAdapter()

    }

    fun loadDataFromFirestoreAndInitializeAdapter() {
        firebaseViewModel.firestore.collection("Profile").document(firebaseViewModel.user.value!!.email!!)
            .collection("Notifications")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val loadedData: MutableList<Notification> = mutableListOf()

                for (document in querySnapshot) {
                    val documentID = document.id
                    val notification = document.toObject(Notification::class.java)
                    notification.id = documentID
                    loadedData.add(notification)
                }

                // Adapter wird erstellt und mit den neuen Daten initialisiert
                rvAdapter = NotificationsAdapter(loadedData, firebaseViewModel)
                notificationsRV.adapter = rvAdapter
            }
    }

}