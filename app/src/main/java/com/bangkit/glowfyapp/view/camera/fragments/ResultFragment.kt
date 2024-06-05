package com.bangkit.glowfyapp.view.camera.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.glowfyapp.databinding.FragmentResultBinding
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
    }

    private fun setupAction() {
        binding.fabHome.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun setupView() {
        val args = ConfirmFragmentArgs.fromBundle(requireArguments())
        val imageUri = args.imageUri
        displayImage(imageUri)
    }

    private fun displayImage(imageUri: String) {
        Glide.with(this)
            .load(imageUri)
            .into(binding.resultImage)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}