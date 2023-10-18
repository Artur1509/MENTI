package com.example.menti.util

import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.menti.FirebaseViewModel
import com.example.menti.R
import com.example.menti.data.model.PsychologistProfile
import com.example.menti.databinding.ListItemBinding
import com.example.menti.ui.FavoritesFragmentDirections
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


class FavoritesAdapter(
    var dataset: ArrayList<Pair<String, PsychologistProfile>>,
    var firebaseViewModel: FirebaseViewModel

    ): RecyclerView.Adapter<FavoritesAdapter.ItemViewHolder>() {
    class ItemViewHolder(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        // Dataset
        var item = dataset[position]
        // Dokument Id
        val id = item.first
        // PsychologenProfil
        val profil = item.second

        //CardView Bild und Texte
        val imgUri = profil.bild!!.toUri().buildUpon().scheme("https").build()
        holder.binding.profileTitleTV.text = profil.beruf
        holder.binding.profileNameTV.text = "${profil.titel} ${profil.vorname} ${profil.name}"
        holder.binding.ratingBar4.rating = profil.bewertung!!
        holder.binding.profileImageIV.load(imgUri) {
        }

        // Favoriten BTN - Favorit wieder aus der Liste entfernen
        holder.binding.addToFavoritesBTN.setOnClickListener {
            firebaseViewModel.removeFavorite(id)
            dataset.removeAt(position)
            updateData(dataset)
        }
        // Favorit Symbol ist aktviet wenn Profil in der Favoritenliste ist.
        holder.binding.addToFavoritesBTN.isActivated = true

        //Bei klick auf Cardview weiterleitung zur Detailview
        holder.binding.cardView.setOnClickListener {

            try {
                val navController = holder.itemView.findNavController()
                navController.navigate(
                    FavoritesFragmentDirections.actionFavoritesFragmentToDetailFragment(
                    profilePicture = profil.bild!!,
                    titel = profil.titel!!,
                    name = profil.name!!,
                    vorname = profil.vorname!!,
                    beruf = profil.beruf!!,
                    bewertung = profil.bewertung!!,
                    beschreibung = profil.beschreibung!!,
                    tags = profil.tags!!.toTypedArray()))

            }catch (e: Exception) {
                Log.e("RV", "${e.message}")
            }
        }

        // Für jeden Tag im Psychologenprofil wird ein Chip erstellt und in der Chipview auf der Card angezeigt.
        profil.tags!!.forEach {

            holder.binding.tagsCG.addChip(it)

        }


    }
    override fun getItemCount(): Int {
        return dataset.size
    }

    // Eigenschaften der erstellten Chips
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

    // Update die Daten in der RV
        fun updateData(newData: ArrayList<Pair<String, PsychologistProfile>>) {
            dataset = newData!!
            notifyDataSetChanged()
        }

}