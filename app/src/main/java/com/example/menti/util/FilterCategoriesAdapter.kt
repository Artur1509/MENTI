package com.example.menti.util

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.menti.FirebaseViewModel
import com.example.menti.data.model.Category
import com.example.menti.databinding.FilterListItemBinding

class FilterCategoriesAdapter(
    var dataset: List<Category>,
    var viewModel: FirebaseViewModel

    ): RecyclerView.Adapter<FilterCategoriesAdapter.ItemViewHolder>() {
    class ItemViewHolder(val binding: FilterListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = FilterListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        // Dataset
        val item = dataset[position]

        // Text und Checkbox der Cardview
        holder.binding.categoryNameTV.text = item.name

        holder.binding.categoryCheckbox.setOnClickListener {
            item.isChecked = holder.binding.categoryCheckbox.isChecked
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }


}