package com.bangkit.glowfyapp.view.customview

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.databinding.CameraDialogBinding

class CustomDialogAlert(context: Context): AlertDialog(context) {
    init {
        setCancelable(false)
    }

    private lateinit var binding: CameraDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CameraDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvTitle.text = context.getString(R.string.takeApicTutorial)
        binding.tvDescription.text = context.getString(R.string.takeApicTutorialDescription)
        binding.rightExampleImage.setImageResource(R.drawable.img_placeholder)
        binding.doneButton.setOnClickListener {
            dismiss()
        }
    }
}