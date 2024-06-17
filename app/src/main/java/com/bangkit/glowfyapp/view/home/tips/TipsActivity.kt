package com.bangkit.glowfyapp.view.home.tips

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.databinding.ActivityTipsBinding

class TipsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTipsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTipsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.backButton.setOnClickListener { onBackPressed() }
        setData(listBeautyTips)
    }

    private fun setData(item : ArrayList<TipsData>){
        val layoutManager = LinearLayoutManager(this)
        binding.tipsRv.layoutManager = layoutManager
        val listTipsAdapter = TipsAdapter(item)
        binding.tipsRv.adapter = listTipsAdapter

    }
    private val listBeautyTips: ArrayList<TipsData>
        get() {
            val iconTips = resources.obtainTypedArray(R.array.tipsIcon)
            val beautyTips = resources.getStringArray(R.array.skinTypeTips)
            val beautyDescription = resources.getStringArray(R.array.tipsDesc)
            val listTips = ArrayList<TipsData>()
            for (i in beautyTips.indices) {
                val tips = TipsData(iconTips.getResourceId(i, -1) ,beautyTips[i],beautyDescription[i])
                listTips.add(tips)
            }
            iconTips.recycle()
            return listTips
        }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}