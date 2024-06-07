package com.bangkit.glowfyapp.view.detailProduct

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.data.models.items.ProductItem
import com.bangkit.glowfyapp.databinding.ActivityProductDetailBinding
import com.bumptech.glide.Glide

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupData()
        actionSetup()
    }

    private fun actionSetup() {
        binding.backButton.setOnClickListener { onBackPressed() }
    }

    private fun setupData() {
        val product = intent.getParcelableExtra<ProductItem>("EXTRA_PRODUCT")
        if (product != null) {
            bindProductDetails(product)
        }
    }

    private fun bindProductDetails(product: ProductItem) {
        with(binding) {
            productDetailImageVp.adapter = ImageDetailAdapter(product.images)
            indicatorImageDetail.setViewPager(productDetailImageVp)
            productDetailName.text = product.nama
            productDetailRating.text = product.rating.toString()
            productDetailPrice.text = getString(R.string.productDetailPrice, product.harga.toString())
            productDetailDesc.text = product.deskripsi

            shareButton.setOnClickListener { shareActionHandler(product) }
            typeFormatHandler(product)
        }
    }

    private fun typeFormatHandler(product: ProductItem) {
        when (product.tipe) {
            "normal" -> {
                binding.textTypeDetail.setText(R.string.normal)
                Glide.with(this)
                    .load(R.drawable.ic_normal)
                    .into(binding.imageTypeDetail)
            }
            "acne" -> {
                binding.textTypeDetail.setText(R.string.acne)
                Glide.with(this)
                    .load(R.drawable.ic_acne)
                    .into(binding.imageTypeDetail)
            }
            "oily" -> {
                binding.textTypeDetail.setText(R.string.oily)
                Glide.with(this)
                    .load(R.drawable.ic_oily)
                    .into(binding.imageTypeDetail)
            }
            "dry" -> {
                binding.textTypeDetail.setText(R.string.dry)
                Glide.with(this)
                    .load(R.drawable.ic_dry)
                    .into(binding.imageTypeDetail)
            }
            else -> {
                Toast.makeText(this, "Tidak Menemukan Tipe Produk", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun shareActionHandler(product: ProductItem) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, ("Periksa Produk Rekomendasi Dari Saya : ${product.link}"))
        intent.type = "text/plain"
        startActivity(intent)
    }
}