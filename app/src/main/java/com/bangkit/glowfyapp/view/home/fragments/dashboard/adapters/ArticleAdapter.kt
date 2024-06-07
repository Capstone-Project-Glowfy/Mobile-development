package com.bangkit.glowfyapp.view.home.fragments.dashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.data.models.items.ArticlesItem
import com.bangkit.glowfyapp.databinding.ItemArticleBinding
import com.bumptech.glide.Glide

class ArticleAdapter(private val items: List<ArticlesItem>) : RecyclerView.Adapter<ArticleAdapter.ViewPagerViewHolder>() {

    class ViewPagerViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val item = items[position]
        with(holder) {
            binding.articleTitle.text = item.judul
            Glide.with(itemView.context)
                .load(item.foto)
                .placeholder(R.drawable.img_placeholder)
                .into(binding.articleImage)
        }
    }

    override fun getItemCount() = items.size
}