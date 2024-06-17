package com.bangkit.glowfyapp.view.camera.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.data.models.ResultApi
import com.bangkit.glowfyapp.data.models.response.SkinsItem
import com.bangkit.glowfyapp.databinding.FragmentResultBinding
import com.bangkit.glowfyapp.utils.Utility
import com.bangkit.glowfyapp.utils.ViewModelFactory
import com.bangkit.glowfyapp.utils.dateFormat
import com.bangkit.glowfyapp.view.camera.ScanViewModel
import com.bangkit.glowfyapp.view.home.HomeActivity
import com.bangkit.glowfyapp.view.welcome.WelcomeActivity
import com.bumptech.glide.Glide

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ScanViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSession()
        setupAction()
        onBackPressedHandler()
    }

    private fun onBackPressedHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun setupAction() {
        binding.fabHome.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun getSession() {
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(context, WelcomeActivity::class.java))
                requireActivity().finish()
            } else {
                setupView(user.token)
            }
        }
    }

    private fun setupView(token: String) {
        val args = ResultFragmentArgs.fromBundle(requireArguments())
        displayResult(args.imageUri, args.skinType, args.skinStatus, args.scanDate)
        observeSkinResponse(args.skinType, token)
    }

    private fun mapTypeToSkinName(type: String): String {
        return when (type) {
            "oily" -> "Berminyak"
            "dry" -> "Kering"
            "normal" -> "Normal"
            "acne" -> "Jerawat"
            else -> type
        }
    }

    private fun displayResult(imageUri: String, type: String, skinStatus: String, date: String) {
        val mappedType = mapTypeToSkinName(type)
        val localizedSkin = Utility.getLocalizedSkinName(requireContext(), mappedType)
        Glide.with(this)
            .load(imageUri)
            .into(binding.resultImage)

        binding.skinTextResult.text = skinStatus
        binding.skinTextType.text = resources.getString(R.string.skinTypeDesc, localizedSkin)
        binding.skinDateResult.text = date.dateFormat()
    }

    private fun observeSkinResponse(type: String, token: String) {
        val skinName = mapTypeToSkinName(type)
        viewModel.getSkins(token).observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultApi.Success -> {
                    val skins = result.data.skins
                    val skinItem = skins.find { it.nama == skinName }
                    skinItem?.let {
                        showLoading(false)
                        imageAndColorFormat(it)
                        skinDescriptionFormat(it)
                    }
                }

                is ResultApi.Error -> {
                    showLoading(false)
                    Log.e("ResultFragment", "Error: ${result.error}")
                }

                is ResultApi.Loading -> {
                    showLoading(true)
                }
            }
        }
    }

    private fun skinDescriptionFormat(skin: SkinsItem) {
        binding.skinDescText.text = skin.deskripsi
    }

    private fun imageAndColorFormat(skin: SkinsItem) {
        when(skin.nama) {
            "Normal" -> {
                binding.skinImageType.setImageResource(R.drawable.ic_normal)
                binding.skinTextType.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
            }

            "Jerawat" -> {
                binding.skinImageType.setImageResource(R.drawable.ic_acne)
                binding.skinTextType.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
            }

            "Berminyak" -> {
                binding.skinImageType.setImageResource(R.drawable.ic_oily)
                binding.skinTextType.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.orange
                    )
                )
            }

            "Kering" -> {
                binding.skinImageType.setImageResource(R.drawable.ic_dry)
                binding.skinTextType.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.yellow
                    )
                )
            }

            else -> {
                binding.skinImageType.setImageResource(R.drawable.ic_normal)
                binding.skinTextType.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}