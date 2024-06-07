package com.bangkit.glowfyapp.data.models.items

import com.google.gson.annotations.SerializedName

data class ArticlesResponse(

	@field:SerializedName("articles")
	val articles: List<ArticlesItem>
)

data class ArticlesItem(

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("tahun")
	val tahun: Int,

	@field:SerializedName("author")
	val author: String,

	@field:SerializedName("judul")
	val judul: String,

	@field:SerializedName("isi")
	val isi: String
)
