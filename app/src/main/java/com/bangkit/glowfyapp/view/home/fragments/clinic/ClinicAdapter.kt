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

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(place: ClinicData) {
            itemView.apply {
                findViewById<TextView>(R.id.txtName).text = place.name
                findViewById<TextView>(R.id.txtAddress).text = place.address
                findViewById<ImageView>(R.id.imgIcon).setImageBitmap(place.icon)
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
}