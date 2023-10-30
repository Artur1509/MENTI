package com.example.menti.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import com.example.menti.FirebaseViewModel
import com.example.menti.R
import com.example.menti.data.model.TerminDaten
import com.example.menti.databinding.TermineListItemBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


class TermineAdapter(
    var dataset: List<TerminDaten>,
    var viewModel: FirebaseViewModel,
    var context : Context

): RecyclerView.Adapter<TermineAdapter.ItemViewHolder>() {
    private var selectedChipId: Int = View.NO_ID
    class ItemViewHolder(val binding: TermineListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = TermineListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        // Dataset
        val item = dataset[position]

        holder.binding.datumTV.text = item.datum

        // Hintergrundfarbe jedes 2 items ändern
        if (position%2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.secondary))
        }
        else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }

        // Chip hintergrundfarbe wie background des RV items
        val chipGroup: ChipGroup = holder.binding.uhrzeitenCG
        chipGroup.isSingleSelection
        val background = (holder.itemView.background as ColorDrawable).color

        while(chipGroup.size != item.uhrzeit.size) {
            item.uhrzeit.forEach {
                holder.binding.uhrzeitenCG.addChip(it.zeit)
            }
        }

        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            chip.isCheckable = true

            if (chip.id == selectedChipId) {
                // Dieser Chip ist ausgewählt
                chip.setChipBackgroundColorResource(R.color.primary)
                chip.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)))
            } else {
                // Dieser Chip ist nicht ausgewählt
                chip.chipBackgroundColor = ColorStateList.valueOf(background)
                chip.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary)))
            }

            chip.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    if (chip.id != selectedChipId) {
                        // Neuer Chip ausgewählt, setze die Auswahl zurück und aktualisiere den ausgewählten Chip
                        selectedChipId = chip.id
                        notifyDataSetChanged()// Aktualisiere die Ansicht, um die Änderungen zu reflektieren
                    }
                }
            }
        }

        Log.e("termine", dataset.toString())

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun ChipGroup.addChip(label: String ) {
        Chip(context).apply {

            id = View.generateViewId()
            text = label
            isCheckable = true
            setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary)))
            setChipStrokeColorResource(R.color.primary)
            setEnsureMinTouchTargetSize(false)
            addView(this)
        }
    }




}