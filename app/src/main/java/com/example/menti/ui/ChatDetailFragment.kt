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
import com.google.firebase.firestore.FirebaseFirestoreException
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
    private lateinit var dataset: MutableList<Message>

    private var loadedData: MutableList<Message> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            chatId = it.getString("chatId")
            empfaenger = it.getString("empfaenger")
            absender = it.getString("absender")
        }


        dataset = loadedData
        rvAdapter = ChatDetailAdapter(dataset, firebaseViewModel)
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
        chatDetailRv.adapter = rvAdapter
        eventChangeListener()

        val navBar = requireActivity().findViewById<BottomNavigationView>(com.example.menti.R.id.bottomNavigation)
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

    }

    //Aktuallisiert die Recyclerview jedes mal wenn eine neue Message rein kommt.
    private fun eventChangeListener() {

        firebaseViewModel.firestore.collection("Chats").document(chatId!!).collection("msgList")
            .addSnapshotListener(object : java.util.EventListener,
                com.google.firebase.firestore.EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    var loadedData = mutableListOf<Message>()
                    for(dc: DocumentChange in value?.documentChanges!!) {

                        //if(dc.type == DocumentChange.Type.ADDED) {
                            var message = dc.document.toObject(Message::class.java)
                            dataset.add(message)
                            Log.e("Messenger2", loadedData.toString())



                    }
                    dataset.sortByDescending { it.timeStamp }
                    rvAdapter.updataData(dataset)
                    scrollToBottom()
                }
                })
    }


    private fun scrollToBottom() {
        // Scrolle zur neuesten Nachricht
        chatDetailRv.scrollToPosition(rvAdapter.itemCount -1)
    }
}


