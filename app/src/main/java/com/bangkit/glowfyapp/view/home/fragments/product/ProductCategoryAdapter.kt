package com.bangkit.glowfyapp.view.home.fragments.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.data.models.ProductsItem
import com.bangkit.glowfyapp.databinding.ItemProductBinding
import com.bangkit.glowfyapp.databinding.ItemProductCategoryBinding
import com.bumptech.glide.Glide

class ProductCategoryAdapter(private var listProduct: List<ProductsItem>) : RecyclerView.Adapter<ProductCategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listProduct.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listProduct[position])
    }

    class ViewHolder(private val binding: ItemProductCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductsItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(product.thumbnail)
                    .placeholder(R.drawable.img_placeholder)
                    .centerCrop()
                    .into(productImageCategory)

                productNameCategory.text = product.title
                productPriceCategory.text = "Rp ${product.price}"
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ProductsItem)
    }
}