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
                R.raw.animation_face,
                "Welcome to Glowfy, Your Personal Skincare Companion!",
                "Discover the ultimate solution for all your skincare needs."
            ),
            WelcomeSlide(
                R.raw.product_recommendation,
                "Discover Products Perfectly Matched to Your Skin!",
                "Receive personalized product suggestions to achieve your skincare goals."
            ),
            WelcomeSlide(
                R.raw.face,
                "Uncover Your Unique Skin Type with Our Advanced Detection Technology!",
                "Get accurate insights and tailored skincare recommendations just for you."
            ),
            WelcomeSlide(
                R.raw.animation_locator,
                "Locate Top Skincare Clinics Near You!",
                "Find expert skincare professionals conveniently located in your area."
            ),
            WelcomeSlide(
                R.raw.animation_market,
                "Experience Hassle-Free Shopping with Our Curated Selections!",
                "Access top-quality skincare products with the assurance of credibility and trustworthiness."
            ),
        )
    }

    private fun setupViewPager(slides: List<WelcomeSlide>) {
        adapter = WelcomeAdapter(slides)
        binding.welcomeSlides.adapter = adapter
        binding.indicatorImageDetail.setViewPager(binding.welcomeSlides)
    }
}