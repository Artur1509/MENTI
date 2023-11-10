package com.example.menti.util

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.menti.FirebaseViewModel
import com.example.menti.data.model.Event
import com.example.menti.databinding.EventListItemBinding


class EventAdapter(
    var dataset: MutableList<Event>,
    var firebaseViewModel: FirebaseViewModel,
    var context: Context


) : RecyclerView.Adapter<EventAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: EventListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            EventListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Achtung")
            builder.setMessage("Möchtest du diesen Termin wirklich stornieren?")
            builder.setPositiveButton("Stornieren") { dialog, which ->
                try {

                    firebaseViewModel.deleteEvent(item.id)
                    if (position != RecyclerView.NO_POSITION) {
                        dataset.removeAt(position)
                        notifyItemRemoved(position)
                        Toast.makeText(context, "Dein Termin wurde Storniert.", Toast.LENGTH_SHORT)
                            .show()
                    }
                } catch (e: Exception) {

                    Log.e("EventAdapter", e.message.toString())

                }
            }
            builder.setNegativeButton("Abbrechen") { dialog, which ->
                // Aktion beim Klicken auf die Abbrechen-Schaltfläche
            }
            builder.show()

        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }


}