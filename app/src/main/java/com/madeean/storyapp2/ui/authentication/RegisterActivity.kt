package com.madeean.storyapp2.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.madeean.storyapp2.R
import com.madeean.storyapp2.Utils.hideKeyboard
import com.madeean.storyapp2.Utils.setVisibility
import com.madeean.storyapp2.databinding.ActivityRegisterBinding
import com.madeean.storyapp2.ui.ViewModelFactory
import com.madeean.storyapp2.ui.authentication.AuthenticationViewModel.RegisterState
import com.madeean.storyapp2.ui.customview.CustomViewListener
import com.madeean.storyapp2.ui.preference.AuthenticationPreferences
import com.madeean.storyapp2.ui.preference.dataStore

class RegisterActivity : AppCompatActivity(), CustomViewListener {
  private lateinit var binding: ActivityRegisterBinding

  private lateinit var viewModel: AuthenticationViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityRegisterBinding.inflate(layoutInflater)
    setContentView(binding.root)
    binding.run {
      btnRegister.updateButtonText("Register")
      btnRegister.isEnabled = false
    }

    setupViewModel()
    setupObserve()
    setupOnBackPressed()
    setupEditTextListener()
    setupButtonClick()
  }

  private fun setupObserve() {
    viewModel.registerStatus.observe(this) {
      binding.run {
        when (it) {
          is RegisterState.Loading -> {
            progressBar.setVisibility(true)
          }

          is RegisterState.Success -> {
            progressBar.setVisibility(false)
            startActivity(LoginActivity.newIntent(this@RegisterActivity, true))
            finish()
          }

          is RegisterState.Error -> {
            progressBar.setVisibility(false)
            tvError.text = it.data.message
            tvError.setVisibility(true)
          }

          is RegisterState.Idle -> {
            progressBar.setVisibility(false)
          }
        }
      }
    }
  }

  private fun setupViewModel() {
    val pref = AuthenticationPreferences.getInstance(application.dataStore)
    val factory: ViewModelFactory = ViewModelFactory.getInstance(this, pref)
    viewModel = ViewModelProvider(this, factory)[AuthenticationViewModel::class.java]
  }

  private fun setupOnBackPressed() {
    onBackPressedDispatcher.addCallback(
      this,
      object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
          finish()
        }
      })
  }

  private fun setupButtonClick() {
    binding.run {
      btnRegister.setOnClickListener {
        onRegisterClicked()
      }
      tvLogin.setOnClickListener {
        startActivity(Intent(this@RegisterActivity, RegisterActivity::class.java))
      }
      ivBack.setOnClickListener {
        onBackPressedDispatcher.onBackPressed()
      }
      tvError.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_close_24, 0)
      tvError.setOnClickListener {
        tvError.setVisibility(false)
      }
    }
  }

  private fun onRegisterClicked() {
    val data = RegisterPostModel(
      name = binding.etNama.text.toString(),
      email = binding.etEmail.text.toString(),
      password = binding.etPassword.text.toString()
    )
    viewModel.register(data)
  }

  private fun setupEditTextListener() {
    binding.run {
      etEmail.setEmailEditTextListener(this@RegisterActivity)
      etPassword.setPasswordEditTextListener(this@RegisterActivity)
    }
  }

  private fun updateButtonState() {
    val isValidEmail = Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()
    val isValidPassword = binding.etPassword.text.toString().length >= 8
    binding.btnRegister.isEnabled =
      isValidEmail && isValidPassword && binding.etNama.text.toString().isNotEmpty()
  }

  override fun onEmailTextChanged(email: String) {
    updateButtonState()
  }

  override fun onPasswordTextChanged(password: String) {
    updateButtonState()
  }

}