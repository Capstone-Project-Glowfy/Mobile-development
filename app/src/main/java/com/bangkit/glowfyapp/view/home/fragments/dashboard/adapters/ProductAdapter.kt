package com.bangkit.glowfyapp.view.home.fragments.dashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.data.models.items.ProductItem
import com.bangkit.glowfyapp.databinding.ItemProductBinding
import com.bumptech.glide.Glide

class ProductAdapter(private var listProduct: List<ProductItem>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listProduct.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listProduct[position])
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listProduct[holder.adapterPosition])
        }
    }

    class ViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(product.thumbnail)
                    .placeholder(R.drawable.img_placeholder)
                    .centerCrop()
                    .into(productImage)

                productName.text = product.nama
                productRating.text = product.rating.toString()
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ProductItem)
    }
}