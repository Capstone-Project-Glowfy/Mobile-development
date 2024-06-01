package com.bangkit.glowfyapp.view.home.fragments.dashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.data.models.ProductsItem
import com.bangkit.glowfyapp.databinding.ItemArticleBinding
import com.bumptech.glide.Glide

class ArticleAdapter(private val items: List<ProductsItem>) : RecyclerView.Adapter<ArticleAdapter.ViewPagerViewHolder>() {

    class ViewPagerViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val item = items[position]
        with(holder) {
            binding.articleTitle.text = item.title
            Glide.with(itemView.context)
                .load(item.thumbnail)
                .placeholder(R.drawable.img_placeholder)
                .centerCrop()
                .into(binding.articleImage)
        }
    }

    override fun getItemCount() = items.size
}