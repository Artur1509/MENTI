package com.example.menti.util

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.menti.FirebaseViewModel
import com.example.menti.data.model.Message
import com.example.menti.databinding.MessageListItem1Binding

class ChatDetailAdapter(
    var dataset: MutableList<Message>,
    var firebaseViewModel: FirebaseViewModel,


    ) : RecyclerView.Adapter<ChatDetailAdapter.ItemViewHolder>() {

    fun addMessage(message: Message) {
        dataset.add(message)
        notifyItemInserted(dataset.size - 1)
    }

    class ItemViewHolder(val binding: MessageListItem1Binding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            MessageListItem1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        //Dataset
        val item = dataset[position]

        holder.binding.messageTextTV.text = item.message
        holder.binding.messageTimeTV.text = item.erstellungsZeit
        holder.binding.messageDateTV.text = item.erstellungsDatum


    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun updataData() {
        notifyDataSetChanged()
    }

}