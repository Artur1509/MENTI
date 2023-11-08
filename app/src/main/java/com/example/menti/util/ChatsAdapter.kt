package com.example.menti.util

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.menti.FirebaseViewModel
import com.example.menti.data.model.Chat
import com.example.menti.databinding.ChatListItemBinding
import com.example.menti.ui.MessengerFragmentDirections


class ChatsAdapter(
    var dataset: MutableList<Chat>,
    var firebaseViewModel: FirebaseViewModel,


    ) : RecyclerView.Adapter<ChatsAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: ChatListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ChatListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        //Dataset
        val item = dataset[position]

        holder.binding.chatPartnerNameTV.text = "${item.empfaengerName}"
        holder.binding.chatDatumTV.text = "${item.erstellungsDatum}"
        holder.binding.chatUhrzeitTV.text = "${item.erstellungsZeit}"

        holder.itemView.setOnClickListener {
            Log.e("Chat", item.chatId.toString())

            val navController = holder.itemView.findNavController()
            navController.navigate(MessengerFragmentDirections.actionMessengerFragmentToChatDetailFragment())
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }


}