package com.bangkit.glowfyapp.view.home.fragments.clinic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.data.models.ClinicData

class ClinicAdapter(private val places: List<ClinicData>) :
    RecyclerView.Adapter<ClinicAdapter.PlaceViewHolder>() {

    private var clickListener: ClinicItemClickListener? = null

    fun setOnItemClickListener(listener: ClinicItemClickListener) {
        clickListener = listener
    }

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(clinic: ClinicData) {
            itemView.apply {
                findViewById<TextView>(R.id.txtName).text = clinic.name
                findViewById<TextView>(R.id.txtAddress).text = clinic.address
                findViewById<ImageView>(R.id.imgIcon).setImageBitmap(clinic.icon)

                setOnClickListener {
                    clickListener?.onClinicItemClick(clinic)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_clinic, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(places[position])
    }

    override fun getItemCount(): Int {
        return places.size
    }

    interface ClinicItemClickListener {
        fun onClinicItemClick(clinicData: ClinicData)
    }
}