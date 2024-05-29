package com.bangkit.glowfyapp.view.home.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bangkit.glowfyapp.view.home.fragments.FavoriteFragment
import com.bangkit.glowfyapp.view.home.fragments.HomeFragment
import com.bangkit.glowfyapp.view.home.fragments.ProductFragment
import com.bangkit.glowfyapp.view.home.fragments.ProfileFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> ProductFragment()
            2 -> FavoriteFragment()
            3 -> ProfileFragment()
            else -> HomeFragment()
        }
    }
}