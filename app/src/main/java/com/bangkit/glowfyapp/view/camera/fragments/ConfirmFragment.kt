    package com.bangkit.glowfyapp.view.camera.fragments

    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.fragment.app.Fragment
    import androidx.navigation.fragment.findNavController
    import com.bangkit.glowfyapp.R
    import com.bangkit.glowfyapp.databinding.FragmentConfirmBinding
    import com.bumptech.glide.Glide

    class ConfirmFragment : Fragment() {

        private var _binding: FragmentConfirmBinding? = null
        private val binding get() = _binding!!

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentConfirmBinding.inflate(inflater, container, false)
            val root: View = binding.root
            return root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            setupViewAndAction()
        }

        private fun setupViewAndAction() {
            val args = ConfirmFragmentArgs.fromBundle(requireArguments())
            val imageUri = args.imageUri
            displayImage(imageUri)

            binding.scanButton.setOnClickListener {
                val action = ConfirmFragmentDirections.actionConfirmFragmentToResultFragment(imageUri)
                findNavController().navigate(action)
            }
            binding.retakeButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        private fun displayImage(imageUri: String) {
            Glide.with(this)
                .load(imageUri)
                .into(binding.imagePreview)
        }

        override fun onDestroy() {
            super.onDestroy()
            _binding = null
        }
    }