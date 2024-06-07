package com.bangkit.glowfyapp.data.models.items

import com.google.gson.annotations.SerializedName

data class SkinsResponse(

	@field:SerializedName("skins")
	val skins: List<SkinsItem>
)

data class SkinsItem(

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("artikel")
	val artikel: String,

	@field:SerializedName("deskripsi")
	val deskripsi: String
)
