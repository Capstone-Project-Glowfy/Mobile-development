package com.bangkit.glowfyapp.data.models.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScanResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("status")
	val status: String
) : Parcelable

@Parcelize
data class Data(

	@field:SerializedName("message-scan")
	val messageScan: String,

	@field:SerializedName("prediction")
	val prediction: Prediction,

	@field:SerializedName("id-scan")
	val idScan: String,

	@field:SerializedName("Scan-Date")
	val scanDate: String
) : Parcelable

@Parcelize
data class Prediction(

	@field:SerializedName("status-penyakit")
	val statusPenyakit: String,

	@field:SerializedName("status-kulit")
	val statusKulit: String
) : Parcelable