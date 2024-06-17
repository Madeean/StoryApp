package com.madeean.storyapp2.ui.authentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.madeean.storyapp2.R.string
import com.madeean.storyapp2.Utils.setVisibility
import com.madeean.storyapp2.databinding.ActivityLoginBinding
import com.madeean.storyapp2.ui.ViewModelFactory
import com.madeean.storyapp2.ui.authentication.AuthenticationViewModel.LoginState
import com.madeean.storyapp2.ui.customview.CustomViewListener
import com.madeean.storyapp2.ui.home.HomeActivity
import com.madeean.storyapp2.ui.preference.AuthenticationPreferences
import com.madeean.storyapp2.ui.preference.dataStore

class LoginActivity : AppCompatActivity(), CustomViewListener {
  private lateinit var binding: ActivityLoginBinding

  private lateinit var viewModel: AuthenticationViewModel
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)
    binding.run {
      btnLogin.updateButtonText("Login")
      btnLogin.isEnabled = false
    }

    loadIntent()
    setupViewModel()
    setupObserve()
    setupEditTextListener()
    setupButtonClick()
  }

  private fun setupObserve() {
    viewModel.loginStatus.observe(this) {
      binding.run {
        when (it) {
          is LoginState.Loading -> {
            progressBar.setVisibility(true)
          }

          is LoginState.Success -> {
            viewModel.prefSaveToken(it.data.loginResult.token)
            progressBar.setVisibility(false)
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()
          }

          is LoginState.Error -> {
            progressBar.setVisibility(false)
            tvError.text = it.data.message
            tvError.setVisibility(true)
          }

          is LoginState.Idle -> {
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

  private fun loadIntent() {
    if(intent.getBooleanExtra(EXTRA_SHOW_INFORMATION_SUCCESS, false)) binding.tvSuccess.setVisibility(true)
  }

  private fun setupButtonClick() {
    binding.run {
      btnLogin.setOnClickListener {
        val data = LoginPostModel(
          email = etEmail.text.toString(),
          password = etPassword.text.toString()
        )
        viewModel.login(data)
      }
      tvRegister.setOnClickListener {
        startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
      }
      tvError.setOnClickListener {
        tvError.setVisibility(false)
      }
      tvSuccess.setOnClickListener {
        tvError.setVisibility(false)
      }
    }
  }

  private fun setupEditTextListener() {
    binding.run {
      etEmail.setEmailEditTextListener(this@LoginActivity)
      etPassword.setPasswordEditTextListener(this@LoginActivity)
    }
  }

  private fun updateButtonState() {
    val isValidEmail = Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()
    val isValidPassword = binding.etPassword.text.toString().length >= 8
    binding.btnLogin.isEnabled = isValidEmail && isValidPassword
  }

  private fun isValidEmail(email: String): Boolean {
    return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
  }

  companion object {
    const val EXTRA_SHOW_INFORMATION_SUCCESS = "extra_show_information_success"

    fun newIntent(context: Context, data: Boolean? = false): Intent {
      val intent = Intent(context, LoginActivity::class.java)
      intent.putExtra(EXTRA_SHOW_INFORMATION_SUCCESS, data)
      return intent
    }
  }

  override fun onEmailTextChanged(email: String) {
    updateButtonState()
  }

  override fun onPasswordTextChanged(password: String) {
    updateButtonState()
  }
}