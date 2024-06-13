package com.bangkit.glowfyapp.data.models.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class SkinsResponse(

	@field:SerializedName("skins")
	val skins: List<SkinsItem>
)

@Parcelize
data class SkinsItem(

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("artikel")
	val artikel: String,

	@field:SerializedName("deskripsi")
	val deskripsi: String
):Parcelable
