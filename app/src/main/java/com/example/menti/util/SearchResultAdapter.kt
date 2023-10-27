package com.example.menti.util

import android.content.ClipData.Item
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.menti.FirebaseViewModel
import com.example.menti.R
import com.example.menti.data.model.PsychologistProfile
import com.example.menti.databinding.ListItemBinding
import com.example.menti.ui.SearchFragment
import com.example.menti.ui.SearchFragmentDirections
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.firestore.DocumentReference

class SearchResultAdapter(
    var dataset: ArrayList<Pair<DocumentReference, PsychologistProfile>>,
    var firebaseViewModel: FirebaseViewModel

    ): RecyclerView.Adapter<SearchResultAdapter.ItemViewHolder>() {
    class ItemViewHolder(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        //Dataset
        val item = dataset[position]
        //Dokumentenreferenz des Psychologenprofils
        val reference = item.first
        //Psychologenprofil item
        val profil = item.second

        // Profilbild und Text auf der Cardview
        val imgUri = profil.bild!!.toUri().buildUpon().scheme("https").build()
        holder.binding.profileTitleTV.text = profil.beruf
        holder.binding.profileNameTV.text = "${profil.titel} ${profil.vorname} ${profil.name}"
        holder.binding.ratingBar4.rating = profil.bewertung!!
        holder.binding.profileImageIV.load(imgUri) {
        }


        // Favoriten BTN -> Favorit hinzufügen
        holder.binding.addToFavoritesBTN.setOnClickListener {
            firebaseViewModel.addFavorites(reference)
            it.isActivated = true
        }


        // Weiterleitung zum Detailfragment bei klick auf Cardview
        holder.binding.cardView.setOnClickListener {

            try {
                val navController = holder.itemView.findNavController()
                navController.navigate(SearchFragmentDirections.actionSearchFragmentToDetailFragment2(
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

        // Für jeden Tag im Psychologenprofil wird ein Chip erstellt
        profil.tags!!.forEach {

            holder.binding.tagsCG.addChip(it)

        }

        // Prüft ob die Profile aus der Search RV bereits als Favoriten hinzugefügt wurden, wenn ja dann wird das Favoriten Icon verändert.
        val favorites: MutableList<DocumentReference> = mutableListOf()

        firebaseViewModel.firestore.collection("Profile").document(firebaseViewModel.user.value!!.email!!).collection("Favoriten").get().addOnSuccessListener { documents ->

            for(document in documents) {

                favorites.add(document.data["reference"] as DocumentReference)

                holder.binding.addToFavoritesBTN.isActivated = favorites.contains(reference)

            }

        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    // Style der Chips in der Chipgroup auf der Cardview
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

    fun filter(query: List<String>) {
        dataset.filter() {
            it.second.tags!!.containsAll(query)
        }
        notifyDataSetChanged()
    }

}