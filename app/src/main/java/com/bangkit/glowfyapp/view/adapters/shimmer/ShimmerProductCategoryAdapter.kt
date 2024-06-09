package com.bangkit.glowfyapp.view.adapters.shimmer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.glowfyapp.R
import com.facebook.shimmer.ShimmerFrameLayout

class ShimmerProductCategoryAdapter : RecyclerView.Adapter<ShimmerProductCategoryAdapter.SkeletonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkeletonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shimmer_product_category, parent, false)
        return SkeletonViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkeletonViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 10
    }

    class SkeletonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            val shimmerFrameLayout = itemView as ShimmerFrameLayout
            shimmerFrameLayout.startShimmer()
        }
    }
}

