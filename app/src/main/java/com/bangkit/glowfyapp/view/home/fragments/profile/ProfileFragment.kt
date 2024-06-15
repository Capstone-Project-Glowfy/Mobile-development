package com.bangkit.glowfyapp.view.home.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.databinding.FragmentProfileBinding
import com.bangkit.glowfyapp.utils.ViewModelFactory
import com.bangkit.glowfyapp.view.history.ScanHistoryActivity
import com.bangkit.glowfyapp.view.home.HomeViewModel
import com.bangkit.glowfyapp.view.welcome.WelcomeActivity
import com.bumptech.glide.Glide

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swipeRefreshLayout.setOnRefreshListener { getSession() }
        getSession()
    }

    private fun getSession() {
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(requireContext(), WelcomeActivity::class.java))
                requireActivity().finish()
            } else {
                setupAction()
                setupProfile(user.name, user.email)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getSession()
    }

    private fun setupProfile(name: String, email: String) {

        binding.apply {
            userNameTv.text = name
            userEmailTv.text = email
        }

        viewModel.dbProfile.observe(viewLifecycleOwner) { profile ->
            Log.d("ProfileFragment", "setupProfile: $profile")
            profile?.let {
                Glide.with(requireContext())
                    .load(it.profileImage)
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .into(binding.profileImage)
            } ?: run {
                Log.d("ProfileFragment", "No profile found")
            }
        }
        viewModel.getProfile()
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun setupAction() {
        binding.cvLogout.setOnClickListener { logout() }
        binding.cvHistory.setOnClickListener { navigateToHistory() }
        binding.cvProfile.setOnClickListener { navigateToDetailProfile() }

    }

    private fun navigateToDetailProfile() {
        val intent = Intent(requireContext(), ProfileDetailActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToHistory() {
        val intent = Intent(requireContext(), ScanHistoryActivity::class.java)
        startActivity(intent)
    }

    private fun logout() {
        viewModel.logout()
        navigateToWelcome()
        showToast(getString(R.string.logout_success))
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToWelcome() {
        startActivity(Intent(requireContext(), WelcomeActivity::class.java))
    }
}