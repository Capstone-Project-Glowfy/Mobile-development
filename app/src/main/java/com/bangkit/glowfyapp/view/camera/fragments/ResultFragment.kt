package com.bangkit.glowfyapp.view.camera.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.bangkit.glowfyapp.databinding.FragmentResultBinding
import com.bangkit.glowfyapp.view.home.HomeActivity
import com.bumptech.glide.Glide

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

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
        setupView()
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

    private fun setupView() {
        val args = ResultFragmentArgs.fromBundle(requireArguments())
        displayResult(args.imageUri, args.prediction, args.skinStatus)
    }

    private fun displayResult(imageUri: String,prediction: String, skinStatus: String) {
        Glide.with(this)
            .load(imageUri)
            .into(binding.resultImage)

        binding.statusSkin.text = skinStatus
        binding.typeSkin.text = prediction
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}