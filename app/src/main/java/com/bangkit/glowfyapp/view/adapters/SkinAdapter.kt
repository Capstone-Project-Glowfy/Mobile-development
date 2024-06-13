package com.bangkit.glowfyapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.data.models.response.SkinsItem
import com.bangkit.glowfyapp.databinding.ItemSkinBinding
import com.bumptech.glide.Glide

class SkinAdapter(private var listSkin: List<SkinsItem>) : RecyclerView.Adapter<SkinAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSkinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listSkin.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listSkin[position])
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listSkin[holder.adapterPosition])
        }
    }

    class ViewHolder(private val binding: ItemSkinBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(skin: SkinsItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(skin.foto)
                    .placeholder(R.drawable.img_placeholder)
                    .centerCrop()
                    .into(skinImage)

                skinTitle.text = skin.nama
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: SkinsItem)
    }
}