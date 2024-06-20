package com.bangkit.glowfyapp.view.customview

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import com.bangkit.glowfyapp.databinding.DialogLocationBinding

class CustomLocationAlert(
    context: Context,
    private val animation: Int,
    private var message: Int,
    private val positiveText: Int,
    private val negativeText: Int
): AlertDialog(context) {
    init {
        setCancelable(false)
    }

    private lateinit var binding: DialogLocationBinding
    private var positiveClickListener: (() -> Unit)? = null
    private var negativeClickListener: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.animation.setAnimation(animation)
        binding.messageAlert.text = context.getString(message)

        binding.btnLocation.text = context.getString(positiveText)
        binding.btnCancel.text = context.getString(negativeText)
        binding.btnLocation.setOnClickListener {
            positiveClickListener?.invoke()
            dismiss()
        }
        binding.btnCancel.setOnClickListener {
            negativeClickListener?.invoke()
            dismiss()
        }
    }

    fun setPositiveClickListener(listener: () -> Unit) {
        positiveClickListener = listener
    }

    fun setNegativeClickListener(listener: () -> Unit) {
        negativeClickListener = listener
    }
}