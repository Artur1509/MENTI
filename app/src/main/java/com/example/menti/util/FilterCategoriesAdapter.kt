package com.example.menti.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.menti.data.model.Category
import com.example.menti.databinding.FilterListItemBinding

class FilterCategoriesAdapter(
    var dataset: List<Category>,

    ): RecyclerView.Adapter<FilterCategoriesAdapter.ItemViewHolder>() {
    class ItemViewHolder(val binding: FilterListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = FilterListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]

        holder.binding.categoryNameTV.text = item.name
        holder.binding.categoryCheckbox.isChecked = item.isChecked

    }

    override fun getItemCount(): Int {
        return dataset.size
    }


}