package com.bangkit.glowfyapp.view.home.tips

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.databinding.ItemTipsBinding

class TipsAdapter(private val listTips: List<TipsData>) : RecyclerView.Adapter<TipsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTipsBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val tips = listTips[position]
        viewHolder.bind(tips)
        viewHolder.binding.tipsCv.setOnClickListener {
            tips.isVisible = !tips.isVisible
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = listTips.size

    inner class ViewHolder(val binding: ItemTipsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tips: TipsData) {
            binding.apply {
                iconTips.setImageResource(tips.icon)
                skinTipsType.text = tips.skinType
                tipsDescription.text = tips.tips
                tipsDescription.visibility = if (tips.isVisible) View.VISIBLE else View.GONE
                dropDown.setImageResource(if (tips.isVisible) R.drawable.ic_up else R.drawable.ic_down)
            }
        }
    }
}

data class TipsData(
    var icon: Int,
    var skinType: String,
    var tips: String,
    var isVisible: Boolean = false
)