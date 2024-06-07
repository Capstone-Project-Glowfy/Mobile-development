package com.bangkit.glowfyapp.data.models.items

import com.google.gson.annotations.SerializedName

data class ProductResponse(

	@field:SerializedName("product")
	val product: List<ProductItem>
)

data class ProductItem(

	@field:SerializedName("thumbnail")
	val thumbnail: String,

	@field:SerializedName("images")
	val images: List<String>,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("harga")
	val harga: Int,

	@field:SerializedName("rating")
	val rating: Float,

	@field:SerializedName("link")
	val link: String,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("tipe")
	val tipe: String
)
