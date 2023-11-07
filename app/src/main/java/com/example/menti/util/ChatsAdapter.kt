package com.example.menti.util

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.menti.FirebaseViewModel
import com.example.menti.data.model.Chat
import com.example.menti.databinding.ChatListItemBinding
import com.example.menti.databinding.EventListItemBinding

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

        holder.itemView.setOnClickListener {
            Log.e("Chat", item.chatId.toString())
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }


}