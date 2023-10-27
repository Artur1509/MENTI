package com.example.menti.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.menti.FirebaseViewModel
import com.example.menti.data.model.Leistung
import com.example.menti.databinding.LeistungListItemBinding

class LeistungenAdapter(
    var dataset: List<Leistung>,
    var viewModel: FirebaseViewModel

): RecyclerView.Adapter<LeistungenAdapter.ItemViewHolder>() {

    var lastcheckedposition: Int = 0

    class ItemViewHolder(val binding: LeistungListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = LeistungListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        // Dataset
        val item = dataset[position]

        holder.binding.leistungTV.text = item.beschreibung
        holder.binding.preisTV.text = item.preis
        holder.binding.checkBox.isChecked = item.selected

    }

    override fun getItemCount(): Int {
        return dataset.size
    }


}


