package com.bangkit.glowfyapp.view.history

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.databinding.ActivityScanHistoryBinding
import com.bangkit.glowfyapp.utils.ViewModelFactory
import com.bangkit.glowfyapp.view.camera.ScanViewModel

class ScanHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanHistoryBinding
    private lateinit var adapter: ScanHistoryAdapter

    private val viewModel by viewModels<ScanViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanHistoryBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setDataView()
        binding.backButton.setOnClickListener { onBackPressed() }
    }

    private fun setDataView() {
        binding.scanHistoryRv.layoutManager = LinearLayoutManager(this)
        adapter = ScanHistoryAdapter(
            onItemClick = { scanHistoryItem ->
                val intent = Intent(this, ScanHistoryDetail::class.java)
                intent.putExtra("SCAN_HISTORY_ID", scanHistoryItem.id)
                startActivity(intent)
            },
            onDeleteClick = { scanHistoryItem ->
                viewModel.deleteScanHistory(scanHistoryItem.id)
                Toast.makeText(this, resources.getString(R.string.deleteSuccess), Toast.LENGTH_SHORT).show()
            }
        )

        binding.scanHistoryRv.adapter = adapter

        viewModel.scanHistory.observe(this) { scanHistory ->
            adapter.submitList(scanHistory)
        }

        viewModel.getScanHistory()
    }
}