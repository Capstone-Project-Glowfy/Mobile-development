package com.bangkit.glowfyapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.data.models.items.ArticlesItem
import com.bangkit.glowfyapp.data.models.items.SkinsItem
import com.bangkit.glowfyapp.databinding.ItemArticleBinding
import com.bumptech.glide.Glide

class ArticleAdapter(private val items: List<ArticlesItem>) : RecyclerView.Adapter<ArticleAdapter.ViewPagerViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

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

            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(items[holder.adapterPosition])
            }
        }
    }

    override fun getItemCount() = items.size

    interface OnItemClickCallback {
        fun onItemClicked(data: ArticlesItem)
    }
}