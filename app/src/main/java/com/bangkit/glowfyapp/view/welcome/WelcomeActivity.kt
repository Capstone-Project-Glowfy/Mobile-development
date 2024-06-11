package com.bangkit.glowfyapp.view.welcome

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.databinding.ActivityWelcomeBinding
import com.bangkit.glowfyapp.view.adapters.WelcomeAdapter
import com.bangkit.glowfyapp.view.adapters.WelcomeSlide

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var adapter: WelcomeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupView()
        setupData()
    }

    private fun setupData() {
        binding.startedBtn.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }

    private fun setupView() {
        val slides = createSlides()
        setupViewPager(slides)
    }

    private fun createSlides(): List<WelcomeSlide> {
        return listOf(
            WelcomeSlide(
                R.raw.animation_skin_care,
                "Welcome to Glowfy! Your personal skincare companion.",
                "Selamat datang di Glowfy! Teman perawatan kulit pribadi Anda."
            ),
            WelcomeSlide(
                R.raw.test4,
                "Discover the perfect skincare routine tailored to your skin type.",
                "Temukan rutinitas perawatan kulit yang sempurna yang disesuaikan dengan jenis kulit Anda."
            ),
            WelcomeSlide(
                R.raw.test5,
                "Get personalized product recommendations for a healthier, glowing skin.",
                "Dapatkan rekomendasi produk yang dipersonalisasi untuk kulit yang lebih sehat dan bercahaya."
            ),
            WelcomeSlide(
                R.raw.test6,
                "Easily detect your skin type with our advanced technology.",
                "Deteksi jenis kulit Anda dengan mudah menggunakan teknologi canggih kami."
            ),
            WelcomeSlide(
                R.raw.test7,
                "Get personalized product recommendations for a healthier, glowing skin.",
                "Dapatkan rekomendasi produk yang dipersonalisasi untuk kulit yang lebih sehat dan bercahaya."
            ),
            WelcomeSlide(
                R.raw.test8,
                "Get personalized product recommendations for a healthier, glowing skin.",
                "Dapatkan rekomendasi produk yang dipersonalisasi untuk kulit yang lebih sehat dan bercahaya."
            ),
            WelcomeSlide(
                R.raw.test10,
                "Get personalized product recommendations for a healthier, glowing skin.",
                "Dapatkan rekomendasi produk yang dipersonalisasi untuk kulit yang lebih sehat dan bercahaya."
            ),
            WelcomeSlide(
                R.raw.test11,
                "Get personalized product recommendations for a healthier, glowing skin.",
                "Dapatkan rekomendasi produk yang dipersonalisasi untuk kulit yang lebih sehat dan bercahaya."
            ),
            WelcomeSlide(
                R.raw.test12,
                "Get personalized product recommendations for a healthier, glowing skin.",
                "Dapatkan rekomendasi produk yang dipersonalisasi untuk kulit yang lebih sehat dan bercahaya."
            ),
            WelcomeSlide(
                R.raw.test13,
                "Get personalized product recommendations for a healthier, glowing skin.",
                "Dapatkan rekomendasi produk yang dipersonalisasi untuk kulit yang lebih sehat dan bercahaya."
            ),
            WelcomeSlide(
                R.raw.test14,
                "Get personalized product recommendations for a healthier, glowing skin.",
                "Dapatkan rekomendasi produk yang dipersonalisasi untuk kulit yang lebih sehat dan bercahaya."
            ),
            WelcomeSlide(
                R.raw.animation_loading_data,
                "Get personalized product recommendations for a healthier, glowing skin.",
                "Dapatkan rekomendasi produk yang dipersonalisasi untuk kulit yang lebih sehat dan bercahaya."
            ),
            WelcomeSlide(
                R.raw.mapanim,
                "Get personalized product recommendations for a healthier, glowing skin.",
                "Dapatkan rekomendasi produk yang dipersonalisasi untuk kulit yang lebih sehat dan bercahaya."
            ),
            WelcomeSlide(
                R.raw.animation_opening,
                "Get personalized product recommendations for a healthier, glowing skin.",
                "Dapatkan rekomendasi produk yang dipersonalisasi untuk kulit yang lebih sehat dan bercahaya."
            ),
        )
    }

    private fun setupViewPager(slides: List<WelcomeSlide>) {
        adapter = WelcomeAdapter(slides)
        binding.welcomeSlides.adapter = adapter
        binding.indicatorImageDetail.setViewPager(binding.welcomeSlides)
    }
}