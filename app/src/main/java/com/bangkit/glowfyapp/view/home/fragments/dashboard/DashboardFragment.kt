package com.bangkit.glowfyapp.view.home.fragments.dashboard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.databinding.FragmentDashboardBinding
import com.bangkit.glowfyapp.view.home.adapters.ArticlesItem
import com.bangkit.glowfyapp.view.home.adapters.ViewPagerArticleAdapter

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var currentPage = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupView()

        return root
    }

    private fun setupView() {
        val items = listOf(
            ArticlesItem(R.drawable.ic_launcher_background, "text 1"),
            ArticlesItem(R.drawable.ic_launcher_background, "text 2"),
            ArticlesItem(R.drawable.ic_launcher_background, "text 3")
        )

        val adapter = ViewPagerArticleAdapter(items)
        binding.articleViewPager.adapter = adapter
        binding.circleIndicator.setViewPager(binding.articleViewPager)

        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                if (adapter.itemCount > 0) {
                    currentPage = (currentPage + 1) % adapter.itemCount
                    binding.articleViewPager.setCurrentItem(currentPage, true)
                    handler.postDelayed(this, 3000)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, 3000)
    }
}