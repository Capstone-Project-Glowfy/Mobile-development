package com.bangkit.glowfyapp.view.history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.data.models.ResultApi
import com.bangkit.glowfyapp.data.models.response.SkinsItem
import com.bangkit.glowfyapp.databinding.ActivityScanHistoryDetailBinding
import com.bangkit.glowfyapp.utils.Utility
import com.bangkit.glowfyapp.utils.ViewModelFactory
import com.bangkit.glowfyapp.utils.dateFormat
import com.bangkit.glowfyapp.view.camera.ScanViewModel
import com.bangkit.glowfyapp.view.welcome.WelcomeActivity
import com.bumptech.glide.Glide

class ScanHistoryDetail : AppCompatActivity() {

    private lateinit var binding: ActivityScanHistoryDetailBinding
    private val viewModel by viewModels<ScanViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanHistoryDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getSession()
    }

    private fun getSession() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                setupViewByData(user.token)
                setupAction()
            }
        }
    }


    private fun setupAction() {
        binding.fabBack.setOnClickListener { onBackPressed() }
    }

    private fun mapTypeToSkinName(type: String): String {
        return when (type) {
            "oily" -> "Berminyak"
            "dry" -> "Kering"
            "normal" -> "Normal"
            "acne" -> "Jerawat"
            else -> type
        }
    }

    private fun setupViewByData(token: String) {
        val Id = intent.getIntExtra("SCAN_HISTORY_ID",0)
        Log.d("ScanHistoryDetail", "Id: $Id")

        viewModel.scanHistory.observe(this) { scanHistory ->
            val scanHistoryItem = scanHistory.find { it.id == Id }
            val skinName = mapTypeToSkinName(scanHistoryItem?.statusKulit ?: "")
            val localizedSkinName = Utility.getLocalizedSkinName(this, skinName)
            with(binding) {
                Glide.with(this@ScanHistoryDetail)
                    .load(scanHistoryItem?.scanImage)
                    .into(resultImage)
                skinTextType.text = resources.getString(R.string.resultSkinTypeText, localizedSkinName)
                skinTextResult.text = scanHistoryItem?.statusPenyakit
                skinDateResult.text = scanHistoryItem?.scanDate?.dateFormat()
                dataViewFormat(skinName)
            }
            observeSkinResponse(skinName, token)
        }

        viewModel.getScanHistory()
    }

    private fun observeSkinResponse(skinName: String, token: String) {
        val skinName = mapTypeToSkinName(skinName)
        viewModel.getSkins(token).observe(this) { result ->
            when (result) {
                is ResultApi.Success -> {
                    val skins = result.data.skins
                    val skinItem = skins.find { it.nama == skinName }
                    skinItem?.let {
                        showLoading(false)
                        skinDescriptionFormat(it)
                    }
                }

                is ResultApi.Error -> {
                    showLoading(false)
                    Log.e("ResultFragment", "Error: ${result.error}")
                }

                is ResultApi.Loading -> {
                    showLoading(true)
                }
            }
        }
    }

    private fun skinDescriptionFormat(skin: SkinsItem) {
        binding.skinDescText.text = skin.deskripsi
    }

    private fun dataViewFormat(statusKulit: String?) {
        when(statusKulit){
            "Normal" -> {
                binding.skinImageType.setImageResource(R.drawable.ic_normal)
                binding.skinTextType.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.green
                    )
                )
            }

            "Jerawat" -> {
                binding.skinImageType.setImageResource(R.drawable.ic_acne)
                binding.skinTextType.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.red
                    )
                )
            }

            "Berminyak" -> {
                binding.skinImageType.setImageResource(R.drawable.ic_oily)
                binding.skinTextType.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.orange
                    )
                )
            }

            "Kering" -> {
                binding.skinImageType.setImageResource(R.drawable.ic_dry)
                binding.skinTextType.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.yellow
                    )
                )
            }

            else -> {
                binding.skinImageType.setImageResource(R.drawable.ic_normal)
                binding.skinTextType.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}