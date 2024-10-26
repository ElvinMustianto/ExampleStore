package com.example.connectapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.connectapi.R
import com.example.connectapi.adapter.ProductImageAdapter.ImageViewHolder

@Suppress("DEPRECATION")
class ProductImageAdapter(private val images: List<String>) : RecyclerView.Adapter<ImageViewHolder>() {
    class ImageViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imageViews: ImageView = view.findViewById(R.id.images)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val image = LayoutInflater.from(parent.context).inflate(R.layout.image_slider, parent, false)
        return ImageViewHolder(image)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = images[position]
        if (imageUrl.isNotBlank() && imageUrl != "...") {  // Validasi URL
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .placeholder(com.bumptech.glide.R.color.material_grey_300)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .into(holder.imageViews)
        } else {
            holder.imageViews.setImageResource(R.drawable.ic_launcher_background) // Berikan placeholder jika tidak ada URL valid
        }
    }
}