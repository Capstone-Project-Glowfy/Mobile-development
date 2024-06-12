package com.bangkit.glowfyapp.view.history

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.databinding.ActivityScanHistoryDetailBinding
import com.bangkit.glowfyapp.utils.ViewModelFactory
import com.bangkit.glowfyapp.view.camera.ScanViewModel
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
        setupViewByData()
    }

    private fun setupViewByData() {
        val Id = intent.getIntExtra("SCAN_HISTORY_ID",0)
        Log.d("ScanHistoryDetail", "Id: $Id")

        viewModel.scanHistory.observe(this) { scanHistory ->
            val scanHistoryItem = scanHistory.find { it.id == Id }
            with(binding) {
                Glide.with(this@ScanHistoryDetail)
                    .load(scanHistoryItem?.scanImage)
                    .into(resultImage)
                typeSkin.text = scanHistoryItem?.statusKulit
                statusSkin.text = scanHistoryItem?.statusPenyakit
                scanDateTv.text = scanHistoryItem?.scanDate
                scanIdTv.text = scanHistoryItem?.scanId.toString()
            }
        }

        viewModel.getScanHistory()
    }
}