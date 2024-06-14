package com.bangkit.glowfyapp.view.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.data.models.response.ProfileResponse
import com.bangkit.glowfyapp.databinding.ActivityHomeBinding
import com.bangkit.glowfyapp.view.camera.CameraActivity
import com.bangkit.glowfyapp.view.home.fragments.clinic.ClinicFragment
import com.bangkit.glowfyapp.view.home.fragments.DashboardFragment
import com.bangkit.glowfyapp.view.home.fragments.ProductFragment
import com.bangkit.glowfyapp.view.home.fragments.profile.ProfileFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var backPressedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupView()
    }

    private fun setupView() {
        navbarViewSetup()
        navbarActionSetup()
        backSetup()
    }

    private fun backSetup() {
        val callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    finish()
                } else {
                    Toast.makeText(baseContext, R.string.backAlert, Toast.LENGTH_SHORT).show()
                }
                backPressedTime = System.currentTimeMillis()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun navbarViewSetup() {
        with(binding) {
            bottomNavView.background = null
            bottomNavView.menu.getItem(2).isEnabled = false
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, DashboardFragment())
            .commit()
    }

    private fun navbarActionSetup() {
        binding.bottomNavView.setOnItemSelectedListener { item ->
            val fragment = when(item.itemId) {
                R.id.navigation_Dashboard -> DashboardFragment()
                R.id.navigation_Product -> ProductFragment()
                R.id.navigation_Clinic -> ClinicFragment()
                R.id.navigation_Profile -> ProfileFragment()
                else -> DashboardFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
            true
        }
        binding.cameraFab.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
    }
}