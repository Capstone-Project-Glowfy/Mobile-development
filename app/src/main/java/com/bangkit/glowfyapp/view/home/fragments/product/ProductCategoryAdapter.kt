package com.bangkit.glowfyapp.view.home.fragments.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.data.models.items.ProductItem
import com.bangkit.glowfyapp.databinding.ItemProductCategoryBinding
import com.bangkit.glowfyapp.view.home.fragments.dashboard.adapters.ProductAdapter
import com.bumptech.glide.Glide

class ProductCategoryAdapter(private var listProduct: List<ProductItem>) : RecyclerView.Adapter<ProductCategoryAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: ProductCategoryAdapter.OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: ProductCategoryAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listProduct.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listProduct[position])
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listProduct[holder.adapterPosition])
        }
    }

    class ViewHolder(private val binding: ItemProductCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(product.thumbnail)
                    .placeholder(R.drawable.img_placeholder)
                    .centerCrop()
                    .into(productImageCategory)

                productNameCategory.text = product.nama
                productPriceCategory.text = "Rp ${product.harga}"
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ProductItem)
    }
}