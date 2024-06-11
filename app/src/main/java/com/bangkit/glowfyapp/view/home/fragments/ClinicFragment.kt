package com.bangkit.glowfyapp.view.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.glowfyapp.databinding.FragmentClinicBinding

class ClinicFragment : Fragment() {

    private var _binding: FragmentClinicBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClinicBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}