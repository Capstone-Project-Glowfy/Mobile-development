package com.bangkit.glowfyapp.view.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.glowfyapp.databinding.ItemArticleBinding

class ViewPagerArticleAdapter(private val items: List<ArticlesItem>) : RecyclerView.Adapter<ViewPagerArticleAdapter.ViewPagerViewHolder>() {

    class ViewPagerViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val item = items[position]
        holder.binding.articleImage.setImageResource(item.imageResId)
        holder.binding.articleTitle.text = item.text
    }

    override fun getItemCount() = items.size
}

data class ArticlesItem(val imageResId: Int, val text: String)