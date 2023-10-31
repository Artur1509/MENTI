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
import androidx.core.view.indices
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import com.example.menti.FirebaseViewModel
import com.example.menti.R
import com.example.menti.data.Termine
import com.example.menti.data.model.TerminDaten
import com.example.menti.data.model.Uhrzeit
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

        // Hier werden die Chips anhand der Uhrzeiten im Dataset erstellt, die while schleife verhindert das die chips bei jedem click auf einen chip dupliziert werden.
        while(chipGroup.size != item.uhrzeit.size) {
            item.uhrzeit.forEach {
                holder.binding.uhrzeitenCG.addChip(it.zeit)
            }
        }

        //Hilfsvariablen für Filter der das TerminDaten Objekt raussucht in dem sich die ausgewählte Uhrzeit befindet.
        val termine = Termine()
        val alleTermine = dataset


        // Sorgt dafür das nur ein Chip in der gesamten Recyclerview ausgewählt werden kann, damit die entsprechende Uhrzeit und das datum ins viewmodel übertagen werden können
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            chip.isCheckable = true

            if (chip.id == selectedChipId) {
                // selected chip
                chip.setChipBackgroundColorResource(R.color.primary)
                chip.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)))
            } else {
                // unselected chip
                chip.chipBackgroundColor = ColorStateList.valueOf(background)
                chip.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary)))
            }

            chip.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    if (chip.id != selectedChipId) {
                        // Neuer Chip ausgewählt, setze die Auswahl zurück und aktualisiere den ausgewählten Chip
                        selectedChipId = chip.id

                        // Setzt alle isChecked booleans im dataset auf false
                        for(i in dataset.indices) {
                            dataset[i].uhrzeit.forEach {
                                it.isChecked = false
                            }
                        }
                        // Setzt das item mit dem index des ausgewählten chips auf true
                        item.uhrzeit[chipGroup.indexOfChild(chip)].isChecked = true

                        //Hier wird der ausgewählte Termin ermittelt (Datum + Uhrzeit)
                        val gefilterteTermine = termine.filterCheckedTerminDaten(alleTermine)

                        // Holt die exakte uhrzeit aus dem ausgewählten termin
                        gefilterteTermine.forEach {
                            it.uhrzeit.forEach {
                                if(it.isChecked) {
                                    Log.e("termine1", "${gefilterteTermine.first().datum} ${it.zeit}")
                                }
                            }
                        }

                        notifyDataSetChanged()


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