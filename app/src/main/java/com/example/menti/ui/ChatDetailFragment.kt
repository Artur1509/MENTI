package com.example.menti.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.menti.FirebaseViewModel
import com.example.menti.R
import com.example.menti.data.model.Chat
import com.example.menti.data.model.Message
import com.example.menti.databinding.FragmentChatDetailBinding
import com.example.menti.util.ChatDetailAdapter
import com.example.menti.util.ChatsAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot


class ChatDetailFragment : Fragment() {

    var chatId: String? = ""
    var empfaenger: String? = ""
    var absender: String? = ""

    private lateinit var binding: FragmentChatDetailBinding
    val firebaseViewModel: FirebaseViewModel by activityViewModels()
    private lateinit var chatDetailRv: RecyclerView
    private lateinit var rvAdapter: ChatDetailAdapter

    private var loadedData: MutableList<Message> = mutableListOf()
    private var listenerRegistration: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            chatId = it.getString("chatId")
            empfaenger = it.getString("empfaenger")
            absender = it.getString("absender")
        }

        // Hier wird der Adapter erstellt
        val loadedData: MutableList<Message> = mutableListOf()
        rvAdapter = ChatDetailAdapter(loadedData, firebaseViewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatDetailBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatDetailRv = binding.chatDetailRV

        val navBar =
            requireActivity().findViewById<BottomNavigationView>(com.example.menti.R.id.bottomNavigation)
        navBar.visibility = View.GONE

        binding.chatpartnerNameTV.text = empfaenger

        binding.backToChatsBTN.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.sendBTN.setOnClickListener {
            firebaseViewModel.createMessage(
                chatId!!,
                binding.messageInputET.text.toString(),
                absender!!,
                empfaenger!!
            )
            binding.messageInputET.text?.clear()

        }

        if (rvAdapter.itemCount == 0) {
            // Adapter erst initialisieren, wenn er leer ist
            rvAdapter = ChatDetailAdapter(loadedData, firebaseViewModel)
            chatDetailRv.adapter = rvAdapter
            loadDataFromFirestoreAndInitializeAdapter()
        } else {
            // Wenn der Adapter bereits Daten enthält, starte die Nachrichtenüberwachung
            startListeningForMessages()
        }
    }

    private fun loadDataFromFirestoreAndInitializeAdapter() {
        firebaseViewModel.firestore.collection("Chats").document(chatId!!).collection("msgList")
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Geladene Daten zur vorhandenen Datenliste hinzufügen
                for (document in querySnapshot) {
                    val message = document.toObject(Message::class.java)
                    loadedData.add(message)
                    Log.e("Messenger", message.message.toString())
                }

                // Adapter bereits erstellt, also nicht erneut erstellen
                // Nur notifyDataSetChanged() aufrufen, um die RecyclerView zu aktualisieren
                rvAdapter.notifyDataSetChanged()
            }
    }


    private fun startListeningForMessages() {
        if (listenerRegistration == null) {
            val messagesCollection = firebaseViewModel.firestore
                .collection("Chats")
                .document(chatId!!)
                .collection("msgList")

            listenerRegistration = messagesCollection
                .addSnapshotListener(
                    EventListener<QuerySnapshot> { snapshot, error ->
                        if (error != null) {
                            Log.e("msgFirestore", "Error listening for messages: $error")
                            return@EventListener
                        }

                        snapshot?.documentChanges?.forEach { change ->
                            val message = change.document.toObject(Message::class.java)
                            when (change.type) {
                                DocumentChange.Type.ADDED -> {
                                    // Neue Nachricht hinzugefügt
                                    rvAdapter.addMessage(message)
                                    scrollToBottom()
                                }

                                else -> {

                                }
                            }
                        }
                    }
                )
        }
    }

    private fun scrollToBottom() {
        // Scrolle zur neuesten Nachricht, um sicherzustellen, dass sie sichtbar ist
        chatDetailRv.scrollToPosition(rvAdapter.itemCount - 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        listenerRegistration?.remove()
    }
}


