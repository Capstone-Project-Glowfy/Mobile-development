package com.bangkit.glowfyapp.view.home.fragments.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.data.historydatabase.ProfileEntity
import com.bangkit.glowfyapp.data.models.ResultApi
import com.bangkit.glowfyapp.data.models.response.ProfileResponse
import com.bangkit.glowfyapp.databinding.ActivityProfileDetailBinding
import com.bangkit.glowfyapp.utils.ViewModelFactory
import com.bangkit.glowfyapp.utils.reduceFileImage
import com.bangkit.glowfyapp.utils.uriToFile
import com.bangkit.glowfyapp.view.home.HomeViewModel
import com.bumptech.glide.Glide
import com.yalantis.ucrop.UCrop
import java.io.File

class ProfileDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileDetailBinding
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getSession()
    }

    private fun getSession() {
        viewModel.getSession().observe(this) { user ->
            editProfile(user.token, user.userId)
            setPreviewImage()
        }
    }

    private fun setPreviewImage() {
        viewModel.dbProfile.observe(this) { profile ->
            profile?.let {
                Glide.with(this)
                    .load(it.profileImage)
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .into(binding.profileImage)
            } ?: run {
                Log.d("ProfileFragment", "No profile found")
            }
        }
        viewModel.getProfile()
    }

    private fun editProfile(token: String, id: String) {
        binding.apply {
            changeImageBtn.setOnClickListener { openGallery() }
            saveBtn.setOnClickListener {
                saveProfileImage(token, id)
                observeViewModel()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.profile.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultApi.Loading -> {
                        showLoading(true)
                    }

                    is ResultApi.Success -> {
                        showLoading(false)
                        showToast(result.data.message)
                        saveToDatabaseAndSetProfile(result.data)
                        finish()
                    }

                    is ResultApi.Error -> {
                        showToast(result.error)
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun saveToDatabaseAndSetProfile(data: ProfileResponse) {
        val profileDb = ProfileEntity(
            profileImage = data.img
        )
        viewModel.saveProfile(profileDb)
    }


    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val picImage = Intent.createChooser(intent, "Select An Image")
        galleryResultLauncher.launch(picImage)
    }

    private val galleryResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val selectedImageUri = result.data!!.data
            if (selectedImageUri != null) {
                startCrop(selectedImageUri)
            }
        }
    }

    private fun startCrop(uri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "IMG_" + System.currentTimeMillis()))
        val options = UCrop.Options()
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
        options.setCompressionQuality(90)

        UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(450, 450)
            .withOptions(options)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == UCrop.REQUEST_CROP) {
                val resultUri = UCrop.getOutput(data)
                if (resultUri != null) {
                    binding.profileImage.setImageURI(resultUri)
                    imageUri = resultUri
                }
            } else if (requestCode == UCrop.RESULT_ERROR) {
                val cropError = UCrop.getError(data)
                cropError?.printStackTrace()
            }
        }
    }

    private fun saveProfileImage(token: String, id: String) {
            imageUri?.let { uri ->
                val imageScan = uriToFile(uri, this).reduceFileImage()
                Log.d("Image File", "showImage: ${imageScan.path}")
                viewModel.updateProfile(token, id, imageScan)
            } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressOverlay.visibility = View.VISIBLE
        } else {
            binding.progressOverlay.visibility = View.GONE
        }
    }
}