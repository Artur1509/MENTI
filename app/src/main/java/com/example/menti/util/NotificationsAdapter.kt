package com.example.menti.util

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.menti.FirebaseViewModel
import com.example.menti.data.model.Notification
import com.example.menti.databinding.NotificationListItemBinding
import com.example.menti.ui.HomeFragmentDirections

class NotificationsAdapter(
    var dataset: List<Notification>,
    var firebaseViewModel: FirebaseViewModel,


    ): RecyclerView.Adapter<NotificationsAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: NotificationListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = NotificationListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        //Dataset
        val item = dataset[position]

        holder.binding.notificationDatumTV.text = item.erstellungsdatum
        holder.binding.notificationUhrzeitTV.text = item.erstellungszeit
        holder.binding.notificationInfoTV.text = item.message


        // Löscht die Notification und navigiert zum Events Fragment
        holder.binding.notificationCV.setOnClickListener {
            if(item.typ == "termin") {
                holder.itemView.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToEventsFragment())
                firebaseViewModel.deleteNotification(item.id)

            }
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }



}