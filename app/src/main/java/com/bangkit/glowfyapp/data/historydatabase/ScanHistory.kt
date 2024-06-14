package com.bangkit.glowfyapp.data.historydatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "scan_history")
data class ScanHistory (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @field:SerializedName("id-scan")
    val scanId: String,

    val scanImage: String,

    @field:SerializedName("status-penyakit")
    val statusPenyakit: String,

    @field:SerializedName("status-kulit")
    val statusKulit: String,

    @field:SerializedName("Scan-Date")
    val scanDate: String

)

@Entity(tableName = "profile")
data class ProfileEntity (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val profileImage: String

)