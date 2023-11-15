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
import com.example.menti.data.model.Chat
import com.example.menti.data.model.Event
import com.example.menti.databinding.FragmentHomeBinding
import com.example.menti.databinding.FragmentMessengerBinding
import com.example.menti.util.ChatsAdapter
import com.example.menti.util.EventAdapter
import com.example.menti.util.NotificationsAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class MessengerFragment : Fragment() {

    private lateinit var binding: FragmentMessengerBinding
    val firebaseViewModel: FirebaseViewModel by activityViewModels()
    private lateinit var chatRV: RecyclerView
    private lateinit var rvAdapter: ChatsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Navbar sichtbarkeit
        val navBar =
            requireActivity().findViewById<BottomNavigationView>(com.example.menti.R.id.bottomNavigation)
        navBar.visibility = View.VISIBLE

        navBar.menu.getItem(3).isChecked = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMessengerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navBar =
            requireActivity().findViewById<BottomNavigationView>(com.example.menti.R.id.bottomNavigation)
        navBar.visibility = View.VISIBLE

        chatRV = binding.chatsRV
        loadDataFromFirestoreAndInitializeAdapter()

    }

    fun loadDataFromFirestoreAndInitializeAdapter() {
        firebaseViewModel.firestore.collection("Chats")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val loadedData: MutableList<Chat> = mutableListOf()

                for (document in querySnapshot) {
                    val documentId = document.id
                    val chat = document.toObject(Chat::class.java)
                    chat.chatId = documentId
                    loadedData.add(chat)
                }

                // Adapter wird erstellt und mit den neuen Daten initialisiert
                rvAdapter = ChatsAdapter(loadedData, firebaseViewModel)
                chatRV.adapter = rvAdapter
                chatRV.adapter!!.notifyDataSetChanged()
            }
    }
}