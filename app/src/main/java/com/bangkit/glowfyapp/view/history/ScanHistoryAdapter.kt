package com.bangkit.glowfyapp.view.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.glowfyapp.data.historydatabase.ScanHistory
import com.bangkit.glowfyapp.databinding.ItemScanHistoryBinding
import com.bangkit.glowfyapp.utils.dateFormat
import com.bumptech.glide.Glide

class ScanHistoryAdapter(
    private val onItemClick: (ScanHistory) -> Unit,
    private val onDeleteClick: (ScanHistory) -> Unit
) : ListAdapter<ScanHistory, ScanHistoryAdapter.ScanHistoryViewHolder>(ScanHistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemScanHistoryBinding.inflate(inflater, parent, false)
        return ScanHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScanHistoryViewHolder, position: Int) {
        val scanHistoryItem = getItem(position)
        holder.bind(scanHistoryItem)
    }

    inner class ScanHistoryViewHolder(private val binding: ItemScanHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val scanHistoryItem = getItem(position)
                    onItemClick.invoke(scanHistoryItem)
                }
            }
            binding.deleteBtn.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val scanHistoryItem = getItem(position)
                    onDeleteClick.invoke(scanHistoryItem)
                }
            }
        }

        fun bind(scanHistoryItem: ScanHistory) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(scanHistoryItem.scanImage)
                    .into(scanImageView)

                statusKulitTv.text = scanHistoryItem.statusKulit
                statusPenyakitTv.text = scanHistoryItem.statusPenyakit
                scanDateTv.text = scanHistoryItem.scanDate.dateFormat()
            }
        }
    }

    class ScanHistoryDiffCallback : DiffUtil.ItemCallback<ScanHistory>() {
        override fun areItemsTheSame(oldItem: ScanHistory, newItem: ScanHistory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ScanHistory, newItem: ScanHistory): Boolean {
            return oldItem == newItem
        }
    }
}