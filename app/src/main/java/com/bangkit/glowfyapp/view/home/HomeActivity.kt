package com.bangkit.glowfyapp.view.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.databinding.ActivityHomeBinding
import com.bangkit.glowfyapp.view.home.adapters.ViewPagerAdapter

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        setupView()

    }

    private fun setupView() {
        navbarViewSetup()
        navbarActionSetup()
    }

    private fun navbarViewSetup() {
        with(binding) {
            bottomNavView.background = null
            bottomNavView.menu.getItem(2).isEnabled = false

            fragmentViewPager.adapter = ViewPagerAdapter(this@HomeActivity)
        }
    }

    private fun navbarActionSetup() {
        binding.bottomNavView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.homeNav -> binding.fragmentViewPager.currentItem = 0
                R.id.productNav -> binding.fragmentViewPager.currentItem = 1
                R.id.favoriteNav -> binding.fragmentViewPager.currentItem = 2
                R.id.profileNav -> binding.fragmentViewPager.currentItem = 3
            }
            true
        }

        binding.fragmentViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val menuItemId = when(position) {
                    0 -> R.id.homeNav
                    1 -> R.id.productNav
                    2 -> R.id.favoriteNav
                    3 -> R.id.profileNav
                    else -> R.id.homeNav
                }
                binding.bottomNavView.selectedItemId = menuItemId
            }
        })
    }
}