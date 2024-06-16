package com.bangkit.glowfyapp.view.customview

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.bangkit.glowfyapp.databinding.DialogAlertBinding

class CustomAlertDialog(context: Context, private val animation: Int, private var message: Int, private val actionText: Int): AlertDialog(context) {
    init {
        setCancelable(false)
    }

    private lateinit var binding: DialogAlertBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogAlertBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.animation.setAnimation(animation)
        binding.messageAlert.text = context.getString(message)

        binding.btnNext.text = context.getString(actionText)
        binding.btnNext.setOnClickListener {
            dismiss()
        }
    }
}