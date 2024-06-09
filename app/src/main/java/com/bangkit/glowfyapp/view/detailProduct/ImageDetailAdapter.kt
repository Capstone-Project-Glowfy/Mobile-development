package com.bangkit.glowfyapp.view.detailProduct

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.data.models.items.ArticlesItem
import com.bangkit.glowfyapp.databinding.ItemArticleBinding
import com.bangkit.glowfyapp.databinding.ItemProductDetailBinding
import com.bumptech.glide.Glide

class ImageDetailAdapter(private val images: List<String>) : RecyclerView.Adapter<ImageDetailAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageDetailViewPager)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_detail, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = images[position]
        Glide.with(holder.imageView.context)
            .load(imageUrl)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return images.size
    }
}