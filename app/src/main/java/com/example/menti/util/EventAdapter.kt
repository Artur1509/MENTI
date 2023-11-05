package com.example.menti.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.menti.FirebaseViewModel
import com.example.menti.data.model.Event
import com.example.menti.databinding.EventListItemBinding


class EventAdapter(
    var dataset: List<Event>,
    var firebaseViewModel: FirebaseViewModel,


    ): RecyclerView.Adapter<EventAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: EventListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = EventListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        //Dataset
        val item = dataset[position]

        holder.binding.expertNameTV.text = item.experte
        holder.binding.terminArtTV.text = item.leistung
        holder.binding.eventDatumTV.text = item.datum
        holder.binding.eventUhrzeitTV.text = item.uhrzeit

        holder.binding.stornierenBTN.setOnClickListener {
            firebaseViewModel.deleteEvent(item.id)
            notifyItemRemoved(position)
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }



}