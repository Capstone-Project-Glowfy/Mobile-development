    package com.bangkit.glowfyapp.view.camera.fragments

    import android.content.Intent
    import android.net.Uri
    import android.os.Bundle
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Toast
    import androidx.activity.addCallback
    import androidx.fragment.app.Fragment
    import androidx.fragment.app.viewModels
    import androidx.navigation.fragment.findNavController
    import com.bangkit.glowfyapp.R
    import com.bangkit.glowfyapp.data.historydatabase.ScanHistory
    import com.bangkit.glowfyapp.data.models.ResultApi
    import com.bangkit.glowfyapp.data.models.response.ScanResponse
    import com.bangkit.glowfyapp.databinding.FragmentConfirmBinding
    import com.bangkit.glowfyapp.utils.ViewModelFactory
    import com.bangkit.glowfyapp.utils.reduceFileImage
    import com.bangkit.glowfyapp.utils.uriToFile
    import com.bangkit.glowfyapp.view.camera.ScanViewModel
    import com.bangkit.glowfyapp.view.home.HomeActivity
    import com.bumptech.glide.Glide

    class ConfirmFragment : Fragment() {

        private var _binding: FragmentConfirmBinding? = null
        private val binding get() = _binding!!

        private val viewModel by viewModels<ScanViewModel> {
            ViewModelFactory.getInstance(requireContext())
        }

        private var currentImageUri: Uri? = null

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
            getSession()
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

        private fun getSession() {
            viewModel.getSession().observe(viewLifecycleOwner) { user ->
                setupViewAndAction(user.token)
            }
        }

        private fun setupViewAndAction(token: String) {
            val args = ConfirmFragmentArgs.fromBundle(requireArguments())
            val imageUri = args.imageUri
            currentImageUri = Uri.parse(imageUri)
            displayImage(imageUri)

            binding.scanButton.setOnClickListener {
                scanImage(token)
                observeViewModel()
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

        private fun scanImage(token: String) {
            currentImageUri?.let { uri ->
                val imageScan = uriToFile(uri, requireContext()).reduceFileImage()
                Log.d("Image File", "showImage: ${imageScan.path}")
                viewModel.faceDetection(token, imageScan)
            } ?: showToast(getString(R.string.empty_image_warning))
        }

        private fun observeViewModel() {
            viewModel.scanResult.observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is ResultApi.Loading -> {
                            showLoading(true)
                        }

                        is ResultApi.Success -> {
                            showToast(result.data.status)
                            showLoading(false)
                            saveToDatabase(result.data)
                            navigateToResultFragment(result.data)
                        }

                        is ResultApi.Error -> {
                            showToast(result.error)
                            showLoading(false)
                        }
                    }
                }
            }
        }

        private fun saveToDatabase(scanResponse: ScanResponse) {
            val scanHistory = ScanHistory(
                scanId = scanResponse.data.idScan,
                scanImage = currentImageUri.toString(),
                statusPenyakit = scanResponse.data.prediction.statusPenyakit,
                statusKulit = scanResponse.data.prediction.statusKulit,
                scanDate = scanResponse.data.scanDate
            )
            viewModel.addScanToHistory(scanHistory)
        }

        private fun navigateToResultFragment(scanResponse: ScanResponse) {
            val action = ConfirmFragmentDirections.actionConfirmFragmentToResultFragment(
                imageUri = currentImageUri.toString(),
                skinStatus = scanResponse.data.prediction.statusPenyakit,
                skinType = scanResponse.data.prediction.statusKulit,
                scanDate = scanResponse.data.scanDate
            )
            findNavController().navigate(action)
        }

        private fun showToast(message: String) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        private fun showLoading(isLoading: Boolean) {
            if (isLoading) {
                binding.progressOverlay.visibility = View.VISIBLE
            } else {
                binding.progressOverlay.visibility = View.GONE
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            _binding = null
        }
    }