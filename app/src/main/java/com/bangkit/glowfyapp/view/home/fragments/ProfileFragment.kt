package com.bangkit.glowfyapp.view.home.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.databinding.FragmentProfileBinding
import com.bangkit.glowfyapp.utils.ViewModelFactory
import com.bangkit.glowfyapp.view.auth.LoginActivity
import com.bangkit.glowfyapp.view.home.HomeViewModel

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

        setupData()
        setupAction()
    }

    private fun setupData() {
        getSession()
    }

    private fun setupAction() {
        binding.logout.setOnClickListener { logout() }

        binding.language.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun logout() {
        viewModel.logout()
        navigateToLogin()
        showToast()
    }

    private fun showToast() {
        Toast.makeText(requireContext(), getString(R.string.logout_success), Toast.LENGTH_SHORT)
            .show()
    }

    private fun navigateToLogin() {
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }

    private fun getSession() {
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(context, LoginActivity::class.java))
                requireActivity().finish()
            } else {
                setUsername(user.name)
            }
        }
    }


    @SuppressLint("StringFormatInvalid")
    private fun setUsername(name: String) {
        binding.actualInfo1.text = getString(R.string.example_info, name)
    }


}