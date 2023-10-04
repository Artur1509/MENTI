package com.example.menti.util

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.marginBottom
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.menti.R
import com.example.menti.data.model.PsychologistProfile
import com.example.menti.databinding.ListItemBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class searchResultAdapter(
    var dataset: ArrayList<PsychologistProfile>,

    ): RecyclerView.Adapter<searchResultAdapter.ItemViewHolder>() {
    class ItemViewHolder(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        val imgUri = item.bild!!.toUri().buildUpon().scheme("https").build()

        holder.binding.profileTitleTV.text = item.beruf
        holder.binding.profileNameTV.text = "${item.titel} ${item.vorname} ${item.name}"
        holder.binding.ratingBar4.rating = item.bewertung!!

        holder.binding.profileImageIV.load(imgUri) {

        }

        item.tags!!.forEach {

            holder.binding.tagsCG.addChip(it)

        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun ChipGroup.addChip(label: String ) {
        Chip(context).apply {

            id = View.generateViewId()
            text = label
            isClickable = false
            isFocusable = false
            chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
            setEnsureMinTouchTargetSize(false)
            addView(this)
        }
    }
}