package com.bangkit.glowfyapp.view.camera.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.databinding.FragmentCameraBinding
import com.bangkit.glowfyapp.utils.reduceFileImage
import com.bangkit.glowfyapp.view.camera.CameraActivity
import com.bangkit.glowfyapp.view.customview.CustomDialogAlert
import com.bangkit.glowfyapp.view.home.HomeActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraLifecycle: Camera
    private var scaleGestureDetector: ScaleGestureDetector? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        permissionHandler()
        setupAction()
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
        showDialogHandler()
    }

    private fun setupAction() {
        captureCameraHandler()
        switchCameraHandler()
        binding.backButton.setOnClickListener { requireActivity().finish() }
        binding.infoBtn.setOnClickListener { cameraIntroDialog() }
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

    private fun showDialogHandler() {
        val cameraActivity = requireActivity() as? CameraActivity
        val isDialogShown = cameraActivity?.isCameraIntroDialogShown ?: false

        if (!isDialogShown) {
            cameraIntroDialog()
            cameraActivity?.isCameraIntroDialogShown = true
        }
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (allPermissionsGranted()) {
            startCamera()
        }
        else {
            Toast.makeText(
                requireContext(),
                getString(R.string.permission_not_granted),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun permissionHandler() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            permissionLauncher.launch(REQUIRED_PERMISSIONS)
        }
    }

    private fun switchCameraHandler() {
        binding.switchCamera.setOnClickListener {
            cameraSelector =
                if (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
                    CameraSelector.DEFAULT_BACK_CAMERA
                } else {
                    CameraSelector.DEFAULT_FRONT_CAMERA
                }
            startCamera()
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireContext().externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else requireContext().filesDir!!
    }

    private fun captureCameraHandler() {
        binding.captureScan.setOnClickListener {
            onLoading(true)
            takePhoto()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraLifecycle = cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
                scaleGestureDetector = ScaleGestureDetector(requireContext(), ScaleListener())
                binding.viewFinder.setOnTouchListener { _, event ->
                    scaleGestureDetector?.onTouchEvent(event)
                    true
                }

            } catch (exc: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("Test", "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scale = cameraLifecycle.cameraInfo.zoomState.value?.zoomRatio ?: 1f
            val scaleFactor = detector.scaleFactor
            cameraLifecycle.cameraControl.setZoomRatio(scale * scaleFactor)
            return true
        }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(outputDirectory, SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val rotatedFile = photoFile.reduceFileImage()
                    val savedUri = rotatedFile.toUri()
                    onLoading(false)
                    navigateToConfirm(savedUri)
                }

                override fun onError(exc: ImageCaptureException) {
                    onLoading(false)
                    Toast.makeText(requireContext(), "Gagal mengambil gambar.", Toast.LENGTH_SHORT).show()
                    Log.e("Test", "onError: ${exc.message}")
                }
            }
        )
    }

    private fun navigateToConfirm(resultUri: Uri) {
        val action = CameraFragmentDirections.actionCameraFragmentToConfirmFragment(resultUri.toString())
        findNavController().navigate(action)
    }

    private fun cameraIntroDialog() {
        CustomDialogAlert(requireContext()).show()
    }

    private fun onLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingFrame.visibility = View.VISIBLE
            binding.viewFinder.visibility = View.GONE
        } else {
            binding.loadingFrame.visibility = View.GONE
            binding.viewFinder.visibility = View.VISIBLE
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}