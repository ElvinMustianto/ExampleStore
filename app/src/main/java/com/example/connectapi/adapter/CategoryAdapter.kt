package com.example.connectapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.connectapi.R
import com.example.connectapi.dto.model.Category

class CategoryAdapter(
    private val onClick: (Category) -> Unit
) : ListAdapter<Category, CategoryAdapter.CategoriesViewHolder>(CategoryDiffCallback()) {

    class CategoriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.categories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val categoryView = LayoutInflater.from(parent.context).inflate(R.layout.row_category, parent, false)
        return CategoriesViewHolder(categoryView)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val category = getItem(position)
        holder.textView.text = category.name
        holder.itemView.setOnClickListener {
            onClick(category)
        }
    }

    // Callback untuk membandingkan data kategori
    class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.slug == newItem.slug
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }
}
