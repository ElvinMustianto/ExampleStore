package com.example.connectapi.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.connectapi.R
import com.example.connectapi.data.model.Product

class ProductAdapter(private val onClick: (Product) -> Unit) :
    PagingDataAdapter<Product, ProductAdapter.ProductViewHolder>(ProductCallBack) {

    class ProductViewHolder(itemView: View, val onClick: (Product) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
        private val title: TextView = itemView.findViewById(R.id.title)
        private val brand: TextView = itemView.findViewById(R.id.brand)
        private val price: TextView = itemView.findViewById(R.id.price)

        private var currentProduct: Product? = null

        init {
            itemView.setOnClickListener {
                currentProduct?.let { product ->
                    onClick(product)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            currentProduct = product

            title.text = product.title
            brand.text = product.brand
            price.text = "$ ${product.price}"

            Glide.with(itemView)
                .load(product.thumbnail)
                .centerCrop()
                .into(thumbnail)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_product, parent, false)
        return ProductViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        if (product != null) {
            holder.bind(product)
        }
    }


    object ProductCallBack : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }
    }
}