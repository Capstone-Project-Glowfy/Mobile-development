package com.bangkit.glowfyapp.view.detail.detailSkins

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.data.models.items.SkinsItem
import com.bangkit.glowfyapp.databinding.ActivitySkinsDetailBinding
import com.bumptech.glide.Glide

class SkinsDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySkinsDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySkinsDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupData()
        setupAction()
    }

    private fun setupAction() {
        binding.backButton.setOnClickListener { onBackPressed() }
    }

    private fun setupData() {
        val skin = intent.getParcelableExtra<SkinsItem>("EXTRA_SKIN")
        if (skin != null) {
            bindArticleDetails(skin)
        }
    }

    private fun bindArticleDetails(skin: SkinsItem) {
        with(binding) {
            Glide.with(this@SkinsDetailActivity)
                .load(skin.foto)
                .into(skinDetailImage)
            skinDetailDesc.text = skin.deskripsi
            binding.skinDetailName.text = resources.getString(R.string.skinTypeDesc, skin.nama)
            nameSkinDetailFormat(skin)
            binding.skinDetailLink.text = getString(R.string.linkText, skin.artikel)
            binding.skinDetailLink.setOnClickListener { openSkinLink(skin.artikel) }
        }
    }

    private fun openSkinLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(link)
        startActivity(intent)
    }

    private fun nameSkinDetailFormat(skin : SkinsItem) {
        when (skin.nama) {
            "Normal" -> {
                binding.skinDetailIcon.setImageResource(R.drawable.ic_normal)
                binding.skinDetailName.setTextColor(ContextCompat.getColor(this, R.color.green))
            }
            "Jerawat" -> {
                binding.skinDetailIcon.setImageResource(R.drawable.ic_acne)
                binding.skinDetailName.setTextColor(ContextCompat.getColor(this, R.color.red))
            }
            "Berminyak" -> {
                binding.skinDetailIcon.setImageResource(R.drawable.ic_oily)
                binding.skinDetailName.setTextColor(ContextCompat.getColor(this, R.color.orange))
            }
            "Kering" -> {
                binding.skinDetailIcon.setImageResource(R.drawable.ic_dry)
                binding.skinDetailName.setTextColor(ContextCompat.getColor(this, R.color.yellow))
            }
        }
    }
}