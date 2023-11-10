package com.example.menti.util

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.menti.FirebaseViewModel
import com.example.menti.R
import com.example.menti.data.model.Leistung
import com.example.menti.databinding.LeistungListItemBinding

class LeistungenAdapter(
    var dataset: List<Leistung>,
    var viewModel: FirebaseViewModel

) : RecyclerView.Adapter<LeistungenAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: LeistungListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            LeistungListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        // Dataset
        val item = dataset[position]

        holder.binding.leistungTV.text = item.beschreibung
        holder.binding.preisTV.text = item.preis
        holder.binding.checkBoxCB.isChecked = item.isChecked

        // Nur eine Checkbox auswählbar
        holder.binding.checkBoxCB.setOnClickListener {

            for (i in dataset.indices) {
                dataset[i].isChecked = (i == position)
            }
            holder.binding.checkBoxCB.isChecked = item.isChecked
            notifyDataSetChanged()

        }

        leistungsAuswahl(dataset)


    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun leistungsAuswahl(dataset: List<Leistung>) {
        for (i in dataset) {
            if (i.isChecked) {
                viewModel.saveLeistung(i)
            }
        }
    }

}


