package com.bangkit.glowfyapp.view.customview

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.databinding.DialogCameraBinding

class CustomDialogCamera(context: Context): AlertDialog(context) {
    init {
        setCancelable(false)
    }

    private lateinit var binding: DialogCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvTitle.text = context.getString(R.string.takeApicTutorial)
        binding.doneButton.setOnClickListener {
            dismiss()
        }
    }
}