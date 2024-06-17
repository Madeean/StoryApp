package com.madeean.storyapp2.ui.home.tambah

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.madeean.storyapp2.R
import com.madeean.storyapp2.R.string
import com.madeean.storyapp2.Utils.getImageUri
import com.madeean.storyapp2.Utils.loadUseGifThumb
import com.madeean.storyapp2.Utils.reduceFileImage
import com.madeean.storyapp2.Utils.setVisibility
import com.madeean.storyapp2.Utils.uriToFile
import com.madeean.storyapp2.databinding.ActivityTambahStoryBinding
import com.madeean.storyapp2.ui.ViewModelFactory
import com.madeean.storyapp2.ui.home.HomeViewModel
import com.madeean.storyapp2.ui.home.HomeViewModel.UploadStoryState
import com.madeean.storyapp2.ui.home.model.UploadStoryPostModel
import com.madeean.storyapp2.ui.preference.AuthenticationPreferences
import com.madeean.storyapp2.ui.preference.dataStore
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class TambahStoryActivity : AppCompatActivity() {
  private lateinit var binding: ActivityTambahStoryBinding
  private lateinit var viewModel: HomeViewModel

  private var currentImageUri: Uri? = null
  private lateinit var token: String

  private val requestPermissionLauncher =
    registerForActivityResult(
      ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
      if (isGranted) {
        Toast.makeText(this, getString(string.permission_request_granted), Toast.LENGTH_LONG).show()
      } else {
        Toast.makeText(this, getString(string.permission_request_denied), Toast.LENGTH_LONG).show()
      }
    }

  private fun allPermissionsGranted() =
    ContextCompat.checkSelfPermission(
      this,
      REQUIRED_PERMISSION
    ) == PackageManager.PERMISSION_GRANTED


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityTambahStoryBinding.inflate(layoutInflater)
    setContentView(binding.root)

    checkAllPermission()
    setupViewModel()
    setupObserve()
    setupButton()
  }

  private fun setupObserve() {
    viewModel.getPrefToken().observe(this) {
      token = "Bearer $it"
    }

    viewModel.uploadStoryStatus.observe(this) {
      binding.run {
        when (it) {
          is UploadStoryState.Idle -> progressBar.setVisibility(false)
          is UploadStoryState.Loading -> progressBar.setVisibility(true)
          is UploadStoryState.Success -> {
            progressBar.setVisibility(false)
            Toast.makeText(this@TambahStoryActivity, getString(string.berhasil_menambahkan_story), Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
          }

          is UploadStoryState.Error -> {
            progressBar.setVisibility(false)
            tvError.text = it.data.message
            tvError.setVisibility(true)
            Toast.makeText(this@TambahStoryActivity, getString(string.gagal_menambahkan_story), Toast.LENGTH_SHORT).show()
          }
        }
      }
    }
  }

  private fun checkAllPermission() {
    if (!allPermissionsGranted()) {
      requestPermissionLauncher.launch(REQUIRED_PERMISSION)
    }
  }

  private fun setupButton() {
    binding.run {
      ivBack.setOnClickListener {
        finish()
      }
      btnCamera.setOnClickListener {
        getPhotoByCamera()
      }
      btnGallery.setOnClickListener {
        getPhotoByGallery()
      }
      btnTambahStory.setOnClickListener {
        addStory()
      }
      tvError.setOnClickListener {
        tvError.setVisibility(false)
      }
    }
  }

  private fun addStory() {
    currentImageUri?.let {
      if (binding.etDescription.text.toString().isEmpty()) {
        Toast.makeText(this,
          getString(string.masukkan_description_terlebih_dahulu), Toast.LENGTH_SHORT).show()
        return
      }

      val description =
        binding.etDescription.text.toString().toRequestBody("text/plain".toMediaType())
      val imageFile = uriToFile(it, this).reduceFileImage()
      val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
      val multipartBody = MultipartBody.Part.createFormData(
        "photo",
        imageFile.name,
        requestImageFile
      )

      val data = UploadStoryPostModel(
        description,
        multipartBody
      )

      viewModel.uploadStory(token, data)

    } ?: run {
      Toast.makeText(this, getString(string.masukkan_gambar_terlebih_dahulu), Toast.LENGTH_SHORT).show()
    }

  }

  private fun getPhotoByGallery() {
    launcherGallery.launch(PickVisualMediaRequest(ImageOnly))
  }

  private fun getPhotoByCamera() {
    currentImageUri = getImageUri(this)
    launcherIntentCamera.launch(currentImageUri)
  }

  private val launcherIntentCamera = registerForActivityResult(
    ActivityResultContracts.TakePicture()
  ) { isSuccess ->
    if (isSuccess) {
      showImage()
    }
  }

  private val launcherGallery = registerForActivityResult(
    ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    if (uri != null) {
      currentImageUri = uri
      showImage()
    }
  }

  private fun showImage() {
    currentImageUri?.let {
      binding.ivTambahStory.setImageURI(it)
    }
  }

  private fun setupViewModel() {
    val pref = AuthenticationPreferences.getInstance(application.dataStore)
    val factory: ViewModelFactory = ViewModelFactory.getInstance(this, pref)
    viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
  }

  companion object {
    private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
  }
}